package com.random.randomizer.presentation.screen.spin

import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.random.randomizer.presentation.core.WheelSegmentUiState
import kotlin.math.roundToInt


@Composable
fun SpinScreen(
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
            lazyListState.spinToItem(uiState.targetIndex, screenHeight)
            viewModel.onEvent(SpinUiEvent.SpinFinished)
        }
    }

    val safeContentPadding = WindowInsets.systemBars.asPaddingValues()
    val topPadding = safeContentPadding.calculateTopPadding()
    val bottomPadding = safeContentPadding.calculateBottomPadding()

    SpinScreen(
        wheelSegments = uiState.wheelSegments,
        lazyListState = lazyListState,
        modifier = modifier.padding(
            top = topPadding,
            bottom = bottomPadding
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

private suspend fun LazyListState.spinToItem(index: Int, screenHeight: Float) {
    val offset = (-screenHeight / 2f).roundToInt() + layoutInfo.beforeContentPadding
    animateScrollToItem(index, offset)

    val targetItem = layoutInfo.visibleItemsInfo.find { it.index == index }!!
    animateScrollBy(value = targetItem.size / 2f)
}