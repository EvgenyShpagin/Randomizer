package com.random.randomizer.presentation.screen.spin

import android.util.Log
import androidx.compose.animation.core.FastOutSlowInEasing
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
import kotlin.math.roundToInt


@Composable
fun SpinScreen(
    navigateToResults: (winnerId: Int) -> Unit,
    viewModel: SpinViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val lazyListState = rememberLazyListState()

    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    LaunchedEffect(uiState.targetIndex) {
        if (uiState.targetIndex == -1) {
            return@LaunchedEffect
        }
        val screenHeight = with(density) { configuration.screenHeightDp.dp.toPx() }
        if (uiState.isSpinning) {
            lazyListState.smoothScrollToCalculateItemSizes(
                originListItemCount = uiState.originListSize,
                onItemSizesCalculated = { sizes ->
                    lazyListState.smoothScrollToIndex(
                        targetIndex = uiState.targetIndex,
                        screenHeight = screenHeight,
                        originListSizes = sizes,
                        padding = lazyListState.layoutInfo.mainAxisItemSpacing
                    )
                    viewModel.onEvent(SpinUiEvent.SpinFinished)
                    Log.d(
                        "TAG_1",
                        "first visible index = ${lazyListState.layoutInfo.visibleItemsInfo.first().index}"
                    )
                    delay(1000)
                    navigateToResults(uiState.targetId)
                }
            )
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

private suspend fun LazyListState.smoothScrollToCalculateItemSizes(
    originListItemCount: Int,
    onItemSizesCalculated: suspend (sizes: IntArray) -> Unit
) {
    val averageItemSize = averageItemSize()

    val sizes = IntArray(originListItemCount) { 0 }

    snapshotFlow { layoutInfo.visibleItemsInfo }
        .collect { items ->
            Log.d(
                "TAG_1",
                "smoothScrollToCalculateItemSizes: collected indices ${items.map { it.index }}"
            )
            val lastOriginIndex = originListItemCount - 1
            items.forEach { item ->
                if (item.index < originListItemCount) {
                    sizes[item.index] = item.size
                }
                // Last item size collected
                if (item.index == lastOriginIndex) {
                    onItemSizesCalculated(sizes)
                }
            }
        }

    animateScrollBy(
        value = averageItemSize * originListItemCount.toFloat(),
        animationSpec = tween(
            durationMillis = 2_000,
            easing = FastOutSlowInEasing
        )
    )
}

private suspend fun LazyListState.smoothScrollToIndex(
    targetIndex: Int,
    screenHeight: Float,
    originListSizes: IntArray,
    padding: Int
) {
    val offset = (-screenHeight / 2f).roundToInt() + layoutInfo.beforeContentPadding

    val currentItem = layoutInfo.visibleItemsInfo.firstOrNull()
    var targetItemOffset = 0
    repeat(targetIndex) { index ->
        targetItemOffset += originListSizes[index % originListSizes.count()] + padding
    }
    targetItemOffset += originListSizes[targetIndex % originListSizes.count()] / 2
    Log.d(
        "TAG_1",
        "targetItemOffset = $targetItemOffset, originListSizes=${originListSizes.contentToString()}"
    )
    val distance = targetItemOffset - (currentItem?.offset ?: 0) + offset

    val durationMillis = 4_000

    animateScrollBy(
        value = distance.toFloat(),
        animationSpec = tween(
            durationMillis = durationMillis,
            easing = FastOutSlowInEasing
        )
    )
}

private fun LazyListState.averageItemSize(): Int {
    val visibleItems = layoutInfo.visibleItemsInfo
    if (visibleItems.isEmpty()) return 0
    val totalSize = visibleItems.sumOf { it.size }
    return totalSize / visibleItems.size
}