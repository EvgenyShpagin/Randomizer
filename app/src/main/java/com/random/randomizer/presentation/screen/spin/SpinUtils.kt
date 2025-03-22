package com.random.randomizer.presentation.screen.spin

import androidx.compose.animation.core.EaseOutQuad
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastRoundToInt
import com.random.randomizer.presentation.core.WheelSegmentUiState
import kotlin.random.Random


suspend fun LazyListState.measureWheelSegmentSizes(segmentSizes: IntArray) {
    if (firstVisibleItemIndex != 0) {
        scrollToItem(0)
    }

    while (true) {
        layoutInfo.visibleItemsInfo.fastForEach { itemInfo ->
            if (itemInfo.index <= segmentSizes.lastIndex) {
                segmentSizes[itemInfo.index] = itemInfo.size
            } else {
                return
            }
        }
        // Move first invisible item at the top
        val lastVisibleIndex = layoutInfo.visibleItemsInfo.last().index
        scrollToItem(index = lastVisibleIndex + 1)
    }
}

suspend fun LazyListState.smoothScrollToIndex(
    targetIndex: Int,
    segmentSizes: IntArray,
    screenHeight: Float,
    continueScroll: Boolean,
    durationMillis: Int = getSpinDurationMillis(firstVisibleItemIndex, targetIndex)
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
            easing = if (continueScroll) {
                EaseOutQuad
            } else {
                FastOutSlowInEasing
            }
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