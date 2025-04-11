package com.random.randomizer.presentation.screen.results

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.random.randomizer.presentation.core.WheelSegmentUiState
import com.random.randomizer.presentation.screen.util.ScreenshotPreviewContainer
import com.random.randomizer.presentation.util.PreviewWheelSegmentList

class ResultsScreenPreviewsScreenshots {

    private val wheelSegment = PreviewWheelSegmentList.first()

    @PreviewLightDark
    @Composable
    private fun LightAndDarkTheme_Deletable_Preview() {
        ResultsScreen(wheelSegment = wheelSegment)
    }

    @Preview
    @Composable
    private fun LightTheme_NonDeletable_Preview() {
        ResultsScreen(
            wheelSegment = wheelSegment,
            canDelete = false
        )
    }

    @OptIn(ExperimentalSharedTransitionApi::class)
    @Composable
    private fun ResultsScreen(
        wheelSegment: WheelSegmentUiState,
        canDelete: Boolean = true
    ) {
        ScreenshotPreviewContainer { animatedVisibilityScope ->
            ResultsContent(
                animatedVisibilityScope = animatedVisibilityScope,
                winnerWheelSegment = wheelSegment,
                canDeleteWinner = canDelete,
                navigateBack = {},
                onSpinClicked = {},
                onDeleteAndSpinClicked = {}
            )
        }
    }
}