@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.random.randomizer.presentation.screen.results

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.random.randomizer.presentation.core.WheelSegment
import com.random.randomizer.presentation.core.WheelSegmentUiState
import com.random.randomizer.presentation.util.unionPaddingWithInsets
import com.random.randomizer.presentation.navigation.SharedContentKeys
import com.random.randomizer.presentation.util.HandleUiEffects
import com.random.randomizer.presentation.util.PreviewContainer

@Composable
fun SharedTransitionScope.ResultsScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
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
        animatedVisibilityScope = animatedVisibilityScope,
        winnerWheelSegment = uiState.winnerUiState,
        canDeleteWinner = uiState.canDeleteWinner,
        navigateBack = navigateToHome,
        onSpinClicked = { viewModel.onEvent(ResultsUiEvent.Spin) },
        onDeleteAndSpinClicked = { viewModel.onEvent(ResultsUiEvent.SpinAndDelete) },
        modifier = modifier
    )
}

@Composable
private fun SharedTransitionScope.ResultsContent(
    animatedVisibilityScope: AnimatedVisibilityScope,
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

    val gradientAnimationState = remember {
        MutableTransitionState(false)
            // Start the animation immediately.
            .apply { targetState = true }
    }

    Surface(modifier = modifier) {
        Box(
            modifier = Modifier
                .systemBarsPadding()
                .fillMaxSize()
        ) {
            AnimatedVisibility(
                visibleState = gradientAnimationState,
                enter = fadeIn(animationSpec = tween(500)) +
                        expandVertically(
                            animationSpec = tween(1000),
                            expandFrom = Alignment.CenterVertically
                        ),
                exit = ExitTransition.None,
                modifier = Modifier.align(Alignment.Center)
            ) {
                GradientBackground(
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
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
                    .sharedElement(
                        state = rememberSharedContentState(
                            key = SharedContentKeys.ofSegment(winnerWheelSegment.id)
                        ),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
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
}

@PreviewLightDark
@Composable
private fun ResultsContentPreview() {
    PreviewContainer { animatedVisibilityScope ->
        ResultsContent(
            animatedVisibilityScope = animatedVisibilityScope,
            winnerWheelSegment = WheelSegmentUiState(0, "Title", "Description", null, null),
            canDeleteWinner = true,
            navigateBack = {},
            onSpinClicked = {},
            onDeleteAndSpinClicked = {},
        )
    }
}
