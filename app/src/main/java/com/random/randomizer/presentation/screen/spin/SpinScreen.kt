package com.random.randomizer.presentation.screen.spin

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.isSpecified
import androidx.compose.ui.unit.isUnspecified
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.random.randomizer.presentation.core.RandomizerPane
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

    var containerHeight by remember { mutableStateOf(Dp.Unspecified) }

    LaunchedEffect(areSegmentsMeasured, shouldBeSpinned, containerHeight) {
        if (!areSegmentsMeasured || !shouldBeSpinned || containerHeight.isUnspecified) {
            return@LaunchedEffect
        }
        val isScrollStartedBefore = hasScrollStarted
        if (!isScrollStartedBefore) {
            lazyListState.scrollToItem(0)
            hasScrollStarted = true
        }
        lazyListState.smoothScrollToIndex(
            targetIndex = targetIndex,
            segmentSizes = segmentSizes,
            continueScroll = isScrollStartedBefore
        )
        onSpinFinish()
    }

    val density = LocalDensity.current

    RandomizerPane(
        modifier = modifier.onSizeChanged {
            // After Activity recreation window insets might be 0.dp
            // so scroll distance calculations will be incorrect.
            // Start scroll after each size change in LaunchedEffect
            with(density) { containerHeight = it.height.toDp() }
        }
    ) { color, contentColor ->
        Surface(color = color, contentColor = contentColor) {
            Box {
                SpinWheelSegmentList(
                    wheelSegments = wheelSegments,
                    lazyListState = lazyListState,
                    modifier = Modifier
                        .testTag("Spin Segment List")
                )
                if (containerHeight.isSpecified) {
                    TransformableIndicator(
                        isLoading = !hasScrollStarted,
                        initHeight = containerHeight,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}