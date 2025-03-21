package com.random.randomizer.presentation.screen.spin

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyListState
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

    val lazyListState = rememberLazyListState()

    var segmentSizes by rememberSaveable { mutableStateOf(intArrayOf()) }
    var areSegmentsMeasured by rememberSaveable { mutableStateOf(false) }
    var isReadyToMeasure by rememberSaveable { mutableStateOf(false) }
    var hasScrollToTargetStarted by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (isReadyToMeasure) return@LaunchedEffect

        delay(transitionDurationMs)

        snapshotFlow { lazyListState.layoutInfo.visibleItemsInfo }
            .first { it.isNotEmpty() }

        isReadyToMeasure = true
    }

    LaunchedEffect(uiState.wheelSegments, isReadyToMeasure) {
        if (uiState.wheelSegments.isEmpty() || areSegmentsMeasured || !isReadyToMeasure) {
            return@LaunchedEffect
        }
        segmentSizes = IntArray(uiState.originListSize)
        lazyListState.measureWheelSegmentSizes(segmentSizes)
        areSegmentsMeasured = true
    }

    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    LaunchedEffect(areSegmentsMeasured, uiState.shouldBeSpinned) {
        if (!areSegmentsMeasured || !uiState.shouldBeSpinned) {
            return@LaunchedEffect
        }
        lazyListState.scrollToItem(0)
        hasScrollToTargetStarted = true
        val screenHeight = with(density) { configuration.screenHeightDp.dp.toPx() }
        lazyListState.smoothScrollToIndex(
            targetIndex = uiState.targetIndex,
            segmentSizes = segmentSizes,
            screenHeight = screenHeight
        )
        viewModel.onEvent(SpinUiEvent.SpinFinished)
    }

    SpinScreen(
        wheelSegments = uiState.wheelSegments,
        hasScrollStarted = hasScrollToTargetStarted,
        lazyListState = lazyListState,
        modifier = modifier
    )
}

@Composable
private fun SpinScreen(
    wheelSegments: List<WheelSegmentUiState>,
    hasScrollStarted: Boolean,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState()
) {
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