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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
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
import kotlinx.coroutines.launch
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

    val indexToSizeList by remember {
        derivedStateOf { lazyListState.layoutInfo.visibleItemsInfo.map { it.index to it.size } }
    }

    val originListSizes = remember { mutableStateListOf<Int>() }

    LaunchedEffect(uiState.originListSize) {
        if (uiState.originListSize == 0) {
            return@LaunchedEffect
        }
        originListSizes.addAll(Array(uiState.originListSize) { 0 })
    }

    // IT STARTS MEASURING ONLY WHEN TARGET INDEX GOT
    LaunchedEffect(uiState.targetIndex, lazyListState) {
        if (uiState.targetIndex == -1) {
            return@LaunchedEffect
        }

        Log.d("TAG_1", "Launched")

        val screenHeight = with(density) {
            configuration.screenHeightDp.dp.toPx()
        }
        val lastOriginIndex = uiState.originListSize - 1

        launch {
            snapshotFlow { indexToSizeList }
                .collect { indexToSizeList ->
                    Log.d("TAG_1", "collected data: $indexToSizeList")
                    indexToSizeList.forEach { (index, size) ->
                        if (index < uiState.originListSize) {
                            originListSizes[index] = size
                        }
                        // Last item size collected
                        if (index == lastOriginIndex) {
                            Log.d("TAG_1", "start smooth scroll after reach ${index}")
                            lazyListState.smoothScrollToIndex(
                                targetIndex = uiState.targetIndex,
                                screenHeight = screenHeight,
                                originListSizes = originListSizes.toIntArray(),
                                padding = lazyListState.layoutInfo.mainAxisItemSpacing
                            )
                            viewModel.onEvent(SpinUiEvent.SpinFinished)
                            delay(1000)
                            navigateToResults(uiState.targetId)
                        }
                    }
                }
        }

        launch {
            delay(100)
            if (!areAllMeasured(originListSizes.toIntArray())) {
                Log.d("TAG_1", "Start spin to measure")
                lazyListState.scrollToLastUnmeasured(originListSizes.toIntArray())
            }
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

private fun areAllMeasured(originListSizes: IntArray): Boolean {
    return originListSizes.none { it == 0 }
}

private suspend fun LazyListState.scrollToLastUnmeasured(originListSizes: IntArray) {
    // If some are not measured then start short scroll
    val notMeasuredCount = originListSizes.count { it == 0 }
    if (notMeasuredCount > 0) {
        val measuredItems = originListSizes.filter { it != 0 }
        val averageItemSize = measuredItems.sum() / measuredItems.count()
        animateScrollBy(
            value = notMeasuredCount *
                    (averageItemSize + layoutInfo.mainAxisItemSpacing).toFloat(),
            animationSpec = tween(
                durationMillis = 2_000,
                easing = FastOutSlowInEasing
            )
        )
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
    screenHeight: Float,
    originListSizes: IntArray,
    padding: Int
) {
    val centerOffset = (-screenHeight / 2f).roundToInt() + layoutInfo.beforeContentPadding

    val originListSize = originListSizes.count()

    val currentItem = layoutInfo.visibleItemsInfo.first()
    var targetItemOffset = 0
    for (index in currentItem.index until targetIndex) {
        targetItemOffset += originListSizes[index % originListSize] + padding
    }
    val targetItemHalfSize = originListSizes[targetIndex % originListSize] / 2
    targetItemOffset += targetItemHalfSize
    require(originListSizes.none { it == 0 })
    val distance = targetItemOffset + currentItem.offset + centerOffset
    val durationMillis = 4_000

    animateScrollBy(
        value = distance.toFloat(),
        animationSpec = tween(
            durationMillis = durationMillis,
            easing = FastOutSlowInEasing
        )
    )
}