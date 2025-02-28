package com.random.randomizer.presentation.screen.spin

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
import androidx.compose.runtime.saveable.rememberSaveable
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

    var segmentSizes by rememberSaveable { mutableStateOf(intArrayOf()) }
    var areSegmentsMeasured by rememberSaveable { mutableStateOf(false) }
    var hasScrollToTargetStarted by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(uiState.originListSize) {
        if (segmentSizes.isNotEmpty() || uiState.originListSize == 0) {
            return@LaunchedEffect
        }
        segmentSizes = IntArray(uiState.originListSize)
    }

    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    LaunchedEffect(areSegmentsMeasured) {
        if (areSegmentsMeasured) {
            hasScrollToTargetStarted = true
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

    LaunchedEffect(uiState.originListSize) {
        if (hasScrollToTargetStarted || uiState.originListSize == 0) {
            return@LaunchedEffect
        }

        snapshotFlow { lazyListState.layoutInfo.visibleItemsInfo }
            .takeWhile { !areSegmentsMeasured }
            .collect { visibleItems ->
                visibleItems.forEach { visibleItem ->
                    if (visibleItem.index < uiState.originListSize) {
                        segmentSizes[visibleItem.index] = visibleItem.size
                    }
                    // Last item size collected
                    if (visibleItem.index == uiState.originListSize - 1) {
                        areSegmentsMeasured = true
                    }
                }
            }
    }

    LaunchedEffect(uiState.targetIndex) {
        if (hasScrollToTargetStarted || uiState.targetIndex == -1) {
            return@LaunchedEffect
        }

        if (!areSegmentsMeasured) {
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
