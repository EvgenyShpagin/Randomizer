package com.random.randomizer.presentation.screen.spin

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.util.fastRoundToInt
import com.random.randomizer.presentation.core.WheelSegmentUiState
import kotlin.random.Random

/**
 * Scrolls to the last item which is not measured to find out its size,
 * or does nothing otherwise
 */
suspend fun LazyListState.scrollToLastUnmeasured(
    segmentSizes: IntArray,
    durationMillis: Int = 2000
) {
    val unmeasuredCount = segmentSizes.count { it == 0 }
    if (unmeasuredCount <= 0) return

    val measuredItems = segmentSizes.filter { it != 0 }
    val averageSize = measuredItems.sum() / measuredItems.count().toFloat()

    val firstVisibleItem = layoutInfo.visibleItemsInfo.first()
    val initOffset = layoutInfo.beforeContentPadding + firstVisibleItem.offset
    val itemSpacing = layoutInfo.mainAxisItemSpacing

    animateScrollBy(
        value = initOffset + (averageSize + itemSpacing) * (unmeasuredCount + 1),
        animationSpec = tween(
            durationMillis = durationMillis,
            easing = LinearEasing
        )
    )
    // Scroll again if the last unmeasured item is still not reached
    val lastVisibleItem = layoutInfo.visibleItemsInfo.last()
    if (lastVisibleItem.index < segmentSizes.count() - 1) {
        scrollToLastUnmeasured(segmentSizes, durationMillis)
    }
}

suspend fun LazyListState.smoothScrollToIndex(
    targetIndex: Int,
    segmentSizes: IntArray,
    screenHeight: Float,
    durationMillis: Int = getSpinDurationMillis(targetIndex)
) {
    val firstVisibleItem = layoutInfo.visibleItemsInfo.first()

    val initOffset = layoutInfo.beforeContentPadding + firstVisibleItem.offset
    val targetItemOffset = (firstVisibleItem.index until targetIndex)
        .sumOf { index -> segmentSizes.of(index) + layoutInfo.mainAxisItemSpacing }
    val centeringOffset = -screenHeight / 2f + segmentSizes.of(targetIndex) / 2f

    animateScrollBy(
        value = initOffset + targetItemOffset + centeringOffset,
        animationSpec = tween(
            durationMillis = durationMillis,
            easing = FastOutSlowInEasing
        )
    )
}

private fun IntArray.of(segmentIndex: Int): Int {
    return get(segmentIndex % count())
}

fun List<WheelSegmentUiState>.extendTo(minCount: Int): List<WheelSegmentUiState> {
    require(minCount > 1) { "Minimum count should be greater than 1" }
    val repeatCount = (minCount + size - 1) / size
    return this * repeatCount
}

private operator fun <T> List<T>.times(count: Int): List<T> {
    return List(count) { this }.flatten()
}

fun getSpinDurationMillis(
    firstVisibleIndex: Int,
    targetIndex: Int,
    avgSegmentSpinTime: Int = 110,
    scatter: Double = 0.2,
    maxValue: Int = 12_000
): Int {
    require(
        firstVisibleIndex >= 0 && targetIndex >= 0
                && avgSegmentSpinTime > 0 && scatter > 0 && maxValue > 0
    )
    val spinItemCount = targetIndex - firstVisibleIndex
    val randomMultiplier = Random.nextDouble(from = 1 - scatter, until = 1 + scatter)
    val randomSpinTimeForOneSegment = (avgSegmentSpinTime * randomMultiplier).fastRoundToInt()
    return (spinItemCount * randomSpinTimeForOneSegment).coerceAtMost(maxValue)
}