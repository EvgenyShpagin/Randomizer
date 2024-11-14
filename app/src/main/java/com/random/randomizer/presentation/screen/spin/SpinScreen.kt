package com.random.randomizer.presentation.screen.spin

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.random.randomizer.presentation.core.WheelSegmentUiState


@Composable
fun SpinScreen(
    viewModel: SpinViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SpinScreen(
        wheelSegments = uiState.wheelSegments,
        modifier = modifier
    )
}

@Composable
private fun SpinScreen(
    wheelSegments: List<WheelSegmentUiState>,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState()
) {
    SpinWheelSegmentList(
        wheelSegments = wheelSegments,
        lazyListState = lazyListState,
        modifier = modifier
            .testTag("Spin Segment List")
    )
}