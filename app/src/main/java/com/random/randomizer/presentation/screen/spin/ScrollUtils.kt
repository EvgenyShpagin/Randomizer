package com.random.randomizer.presentation.screen.spin

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.lazy.LazyListState

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
    durationMillis: Int = 5000
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