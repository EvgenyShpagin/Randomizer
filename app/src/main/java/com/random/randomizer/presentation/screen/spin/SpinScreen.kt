package com.random.randomizer.presentation.screen.spin

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first


@Composable
fun SpinScreen(
    navigateToResults: (winnerId: Int) -> Unit,
    viewModel: SpinViewModel,
    transitionDurationMs: Long,
    modifier: Modifier = Modifier
) {
    HandleUiEffects(viewModel.uiEffect) { effect ->
        when (effect) {
            is SpinUiEffect.NavigateToResults -> navigateToResults(effect.winnerId)
        }
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SpinScreen(
        wheelSegments = uiState.wheelSegments,
        shouldBeSpinned = uiState.shouldBeSpinned,
        originListSize = uiState.originListSize,
        transitionDurationMs = transitionDurationMs,
        targetIndex = uiState.targetIndex,
        onSpinFinish = { viewModel.onEvent(SpinUiEvent.SpinFinished) },
        modifier = modifier
    )
}

@Composable
private fun SpinScreen(
    wheelSegments: List<WheelSegmentUiState>,
    shouldBeSpinned: Boolean,
    originListSize: Int,
    targetIndex: Int,
    transitionDurationMs: Long,
    onSpinFinish: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lazyListState = rememberLazyListState()

    var segmentSizes by rememberSaveable { mutableStateOf(intArrayOf()) }
    var areSegmentsMeasured by rememberSaveable { mutableStateOf(false) }
    var isReadyToMeasure by rememberSaveable { mutableStateOf(false) }
    var hasScrollStarted by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (isReadyToMeasure) return@LaunchedEffect

        delay(transitionDurationMs)

        snapshotFlow { lazyListState.layoutInfo.visibleItemsInfo }
            .first { it.isNotEmpty() }

        isReadyToMeasure = true
    }

    LaunchedEffect(wheelSegments, isReadyToMeasure) {
        if (wheelSegments.isEmpty() || areSegmentsMeasured || !isReadyToMeasure) {
            return@LaunchedEffect
        }
        segmentSizes = IntArray(originListSize)
        lazyListState.measureWheelSegmentSizes(segmentSizes)
        areSegmentsMeasured = true
    }

    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    LaunchedEffect(areSegmentsMeasured, shouldBeSpinned) {
        if (!areSegmentsMeasured || !shouldBeSpinned) {
            return@LaunchedEffect
        }
        val isScrollStartedBefore = hasScrollStarted
        if (!isScrollStartedBefore) {
            lazyListState.scrollToItem(0)
            hasScrollStarted = true
        }
        val screenHeight = with(density) { configuration.screenHeightDp.dp.toPx() }
        lazyListState.smoothScrollToIndex(
            targetIndex = targetIndex,
            segmentSizes = segmentSizes,
            screenHeight = screenHeight,
            continueScroll = isScrollStartedBefore
        )
        onSpinFinish()
    }

    Surface(modifier = modifier) {
        Box(modifier = Modifier.systemBarsPadding()) {
            SpinWheelSegmentList(
                wheelSegments = wheelSegments,
                lazyListState = lazyListState,
                modifier = Modifier
                    .testTag("Spin Segment List")
            )
            TransformableIndicator(
                isLoading = !hasScrollStarted,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}