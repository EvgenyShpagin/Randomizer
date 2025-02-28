package com.random.randomizer.presentation.screen.spin

import android.util.Log
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.random.randomizer.presentation.core.WheelSegmentUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.takeWhile


@Composable
fun SpinScreen(
    navigateToResults: (winnerId: Int) -> Unit,
    viewModel: SpinViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val lazyListState = rememberLazyListState()

    var segmentSizes by remember { mutableStateOf(intArrayOf()) }

    LaunchedEffect(uiState.originListSize) {
        if (uiState.originListSize == 0) {
            return@LaunchedEffect
        }
        segmentSizes = IntArray(uiState.originListSize)
    }

    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    LaunchedEffect(areSegmentsMeasured) {
        if (areSegmentsMeasured) {
            val screenHeight = with(density) { configuration.screenHeightDp.dp.toPx() }
            lazyListState.smoothScrollToIndex(
                targetIndex = uiState.targetIndex,
                segmentSizes = segmentSizes,
                screenHeight = screenHeight
            )
            viewModel.onEvent(SpinUiEvent.SpinFinished)
            delay(1000)
            navigateToResults(uiState.targetId)
        }
    }

    LaunchedEffect(uiState.originListSize, lazyListState) {
        if (uiState.originListSize == 0) {
            return@LaunchedEffect
        }

        Log.d("TAG_1", "Launched size collecting")

        snapshotFlow { lazyListState.layoutInfo.visibleItemsInfo }
            .takeWhile { !areSegmentsMeasured }
            .collect { visibleItems ->
                Log.d("TAG_1", "collected data: ${visibleItems.map { it.index to it.size }}")
                visibleItems.forEach { visibleItem ->
                    if (visibleItem.index < uiState.originListSize) {
                        segmentSizes[visibleItem.index] = visibleItem.size
                    }
                    // Last item size collected
                    if (visibleItem.index == uiState.originListSize - 1) {
                        Log.d("TAG_1", "start smooth scroll after reach ${visibleItem.index}")
                        areSegmentsMeasured = true
                    }
                }
            }
    }

    LaunchedEffect(uiState.targetIndex, lazyListState) {
        if (uiState.targetIndex == -1) {
            return@LaunchedEffect
        }

        Log.d("TAG_1", "Launched measure up check")

        if (!areSegmentsMeasured) {
            Log.d("TAG_1", "Start spin to measure")
            lazyListState.scrollToLastUnmeasured(segmentSizes)
        }
    }

    val layoutDirection = LocalLayoutDirection.current

    val safeContentPadding = WindowInsets.systemBars.asPaddingValues()
    val topPadding = safeContentPadding.calculateTopPadding()
    val bottomPadding = safeContentPadding.calculateBottomPadding()
    val startPadding = safeContentPadding.calculateStartPadding(layoutDirection)
    val endPadding = safeContentPadding.calculateEndPadding(layoutDirection)

    SpinScreen(
        wheelSegments = uiState.wheelSegments,
        lazyListState = lazyListState,
        modifier = modifier.padding(
            top = topPadding,
            start = startPadding,
            bottom = bottomPadding,
            end = endPadding
        )
    )
}

/**
 * Scrolls to the last item which is not measured to find out its size,
 * or does nothing otherwise
 */
private suspend fun LazyListState.scrollToLastUnmeasured(
    segmentSizes: IntArray,
    durationMillis: Int = 2000
) {
    val unmeasuredCount = segmentSizes.count { it == 0 }
    if (unmeasuredCount <= 0) return

    val measuredItems = segmentSizes.filter { it != 0 }
    val averageSize = measuredItems.sum() / measuredItems.count().toFloat()

    animateScrollBy(
        value = (averageSize + layoutInfo.mainAxisItemSpacing) * unmeasuredCount,
        animationSpec = tween(
            durationMillis = durationMillis,
            easing = LinearEasing
        )
    )
    // Scroll again if the last unmeasured item is still not reached
    val lastVisibleItem = layoutInfo.visibleItemsInfo.last()
    if (lastVisibleItem.index < segmentSizes.count() - 1) {
        Log.d("TAG_1", "scrollToLastUnmeasured: was scrolled again")
        scrollToLastUnmeasured(segmentSizes, durationMillis)
    }
}

@Composable
private fun SpinScreen(
    wheelSegments: List<WheelSegmentUiState>,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState()
) {
    Box(modifier = modifier) {
        SpinWheelSegmentList(
            wheelSegments = wheelSegments,
            lazyListState = lazyListState,
            modifier = Modifier
                .testTag("Spin Segment List")
        )
        HorizontalDivider(
            Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        )
    }
}

private suspend fun LazyListState.smoothScrollToIndex(
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