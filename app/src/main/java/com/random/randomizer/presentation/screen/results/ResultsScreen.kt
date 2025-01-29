package com.random.randomizer.presentation.screen.results

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.random.randomizer.presentation.core.WheelSegment
import com.random.randomizer.presentation.core.WheelSegmentUiState
import com.random.randomizer.presentation.theme.AppTheme
import com.random.randomizer.presentation.util.DayAndNightPreview
import com.random.randomizer.presentation.util.HandleUiEffects

@Composable
fun ResultsScreen(
    navigateToHome: () -> Unit,
    navigateToSpin: () -> Unit,
    viewModel: ResultsViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HandleUiEffects(viewModel.uiEffect) { effect ->
        when (effect) {
            ResultsUiEffect.NavigateToSpin -> navigateToSpin()
        }
    }

    BackHandler {
        navigateToHome()
    }

    val layoutDirection = LocalLayoutDirection.current

    val safeContentPadding = WindowInsets.systemBars.asPaddingValues()
    val topPadding = safeContentPadding.calculateTopPadding()
    val bottomPadding = safeContentPadding.calculateBottomPadding()
    val startPadding = safeContentPadding.calculateStartPadding(layoutDirection)
    val endPadding = safeContentPadding.calculateEndPadding(layoutDirection)

    ResultsContent(
        winnerWheelSegment = uiState.winnerUiState,
        canDeleteWinner = uiState.canDeleteWinner,
        navigateBack = navigateToHome,
        onSpinClicked = { viewModel.onEvent(ResultsUiEvent.Spin) },
        onDeleteAndSpinClicked = { viewModel.onEvent(ResultsUiEvent.SpinAndDelete) },
        modifier = modifier
            .padding(
                top = topPadding,
                start = startPadding,
                end = endPadding,
                bottom = bottomPadding
            )
            .consumeWindowInsets(safeContentPadding)
    )
}

@Composable
fun ResultsContent(
    winnerWheelSegment: WheelSegmentUiState,
    canDeleteWinner: Boolean,
    navigateBack: () -> Unit,
    onSpinClicked: () -> Unit,
    onDeleteAndSpinClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val layoutDirection = LocalLayoutDirection.current
    val displayCutout = WindowInsets.displayCutout.asPaddingValues()
    val cutoutStartPadding = displayCutout.calculateStartPadding(layoutDirection)
    val cutoutEndPadding = displayCutout.calculateEndPadding(layoutDirection)

    val startPadding = cutoutStartPadding.coerceAtLeast(16.dp)
    val endPadding = cutoutEndPadding.coerceAtLeast(16.dp)

    Box(modifier = modifier.fillMaxSize()) {
        GradientBackground(modifier = Modifier.fillMaxSize())

        ResultsTopAppBar(
            onNavigationClick = navigateBack,
            modifier = Modifier.align(Alignment.TopCenter)
        )
        WheelSegment(
            itemUiState = winnerWheelSegment,
            isClickable = false,
            onClick = {},
            modifier = Modifier
                .align(Alignment.Center)
                .padding(
                    start = startPadding,
                    end = endPadding
                )
        )
        SpinButtons(
            onSpinClicked = onSpinClicked,
            onDeleteAndSpinClicked = onDeleteAndSpinClicked,
            showDeleteButton = canDeleteWinner,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(
                    bottom = 16.dp,
                    start = startPadding,
                    end = endPadding
                )
        )
    }
}

@DayAndNightPreview
@Composable
private fun ResultsContentPreview() {
    AppTheme {
        Surface {
            ResultsContent(
                winnerWheelSegment = WheelSegmentUiState(0, "Title", "Description", null, null),
                canDeleteWinner = true,
                navigateBack = {},
                onSpinClicked = {},
                onDeleteAndSpinClicked = {},
            )
        }
    }
}
