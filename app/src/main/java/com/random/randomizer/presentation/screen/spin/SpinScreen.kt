package com.random.randomizer.presentation.screen.spin

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBarsPadding
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.random.randomizer.presentation.core.WheelSegmentUiState
import com.random.randomizer.presentation.util.HandleUiEffects
import kotlinx.coroutines.flow.takeWhile


@Composable
fun SpinScreen(
    navigateToResults: (winnerId: Int) -> Unit,
    viewModel: SpinViewModel,
    modifier: Modifier = Modifier
) {
    HandleUiEffects(viewModel.uiEffect) { effect ->
        when (effect) {
            is SpinUiEffect.NavigateToResults -> navigateToResults(effect.winnerId)
        }
    }
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

    LaunchedEffect(areSegmentsMeasured, uiState.shouldBeSpinned) {
        if (!areSegmentsMeasured || !uiState.shouldBeSpinned) {
            return@LaunchedEffect
        }
        hasScrollToTargetStarted = true
        val screenHeight = with(density) { configuration.screenHeightDp.dp.toPx() }
        lazyListState.smoothScrollToIndex(
            targetIndex = uiState.targetIndex,
            segmentSizes = segmentSizes,
            screenHeight = screenHeight
        )
        viewModel.onEvent(SpinUiEvent.SpinFinished)
    }

    LaunchedEffect(uiState.originListSize) {
        if (uiState.shouldBeSpinned || uiState.originListSize == 0) {
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

    LaunchedEffect(uiState.shouldBeSpinned) {
        if (hasScrollToTargetStarted || !uiState.shouldBeSpinned || areSegmentsMeasured) {
            return@LaunchedEffect
        }
        lazyListState.scrollToLastUnmeasured(segmentSizes)
    }

    SpinScreen(
        wheelSegments = uiState.wheelSegments,
        lazyListState = lazyListState,
        modifier = modifier.systemBarsPadding()
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
