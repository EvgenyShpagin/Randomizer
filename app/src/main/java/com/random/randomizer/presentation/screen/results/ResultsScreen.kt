package com.random.randomizer.presentation.screen.results

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.random.randomizer.presentation.core.WheelSegment
import com.random.randomizer.presentation.core.WheelSegmentUiState
import com.random.randomizer.presentation.core.unionPaddingWithInsets
import com.random.randomizer.presentation.theme.AppTheme
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

    ResultsContent(
        winnerWheelSegment = uiState.winnerUiState,
        canDeleteWinner = uiState.canDeleteWinner,
        navigateBack = navigateToHome,
        onSpinClicked = { viewModel.onEvent(ResultsUiEvent.Spin) },
        onDeleteAndSpinClicked = { viewModel.onEvent(ResultsUiEvent.SpinAndDelete) },
        modifier = modifier.systemBarsPadding()
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
    val horizontalPaddingModifier = Modifier.unionPaddingWithInsets(
        padding = PaddingValues(horizontal = 16.dp),
        insets = WindowInsets.displayCutout
    )

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
                .then(horizontalPaddingModifier)
        )
        SpinButtons(
            onSpinClicked = onSpinClicked,
            onDeleteAndSpinClicked = onDeleteAndSpinClicked,
            showDeleteButton = canDeleteWinner,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .then(horizontalPaddingModifier)
                .padding(bottom = 16.dp)
        )
    }
}

@PreviewLightDark
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
