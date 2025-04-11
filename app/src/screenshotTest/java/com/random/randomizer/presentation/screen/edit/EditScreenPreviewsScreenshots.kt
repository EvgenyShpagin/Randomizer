package com.random.randomizer.presentation.screen.edit

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.random.randomizer.presentation.core.WheelSegmentUiState
import com.random.randomizer.presentation.screen.util.ScreenshotPreviewContainer
import com.random.randomizer.presentation.util.PreviewWheelSegmentList

class EditScreenPreviewsScreenshots {

    private val emptyWheelSegment = WheelSegmentUiState()
    private val filledWheelSegment = PreviewWheelSegmentList.first()

    @Preview
    @Composable
    private fun LightTheme_Loading_Preview() {
        EditScreen(isLoading = true)
    }

    @Preview
    @Composable
    private fun LightTheme_NonSaveableEmptyContent_Preview() {
        EditScreen(
            segmentUiState = emptyWheelSegment,
            isLoading = false,
            canSave = false
        )
    }

    @PreviewLightDark
    @Composable
    private fun LightAndDarkTheme_SaveableFilledContent_Preview() {
        EditScreen(
            segmentUiState = filledWheelSegment,
            isLoading = false,
            canSave = true
        )
    }

    @OptIn(ExperimentalSharedTransitionApi::class)
    @Composable
    private fun EditScreen(
        segmentUiState: WheelSegmentUiState = WheelSegmentUiState(),
        canSave: Boolean = false,
        isLoading: Boolean = true
    ) {
        EditScreen(
            isLoading = isLoading,
            title = segmentUiState.title,
            description = segmentUiState.description,
            customColor = segmentUiState.customColor,
            image = segmentUiState.image,
            canSave = canSave
        )
    }

    @OptIn(ExperimentalSharedTransitionApi::class)
    @Composable
    private fun EditScreen(
        isLoading: Boolean = false,
        title: String = "",
        description: String = "",
        customColor: Color? = null,
        image: ImageBitmap? = null,
        canSave: Boolean = true
    ) {
        ScreenshotPreviewContainer { animatedVisibilityScope ->
            EditScreen(
                animatedVisibilityScope = animatedVisibilityScope,
                isLoading = isLoading,
                id = 0,
                title = title,
                description = description,
                customColor = customColor,
                image = image,
                canSave = canSave,
                onInputTitle = {},
                onInputDescription = {},
                onPickImage = {},
                onRemoveImage = {},
                onPickBackgroundColor = {},
                onSave = {},
                onDismiss = {},
                snackbarHostState = remember { SnackbarHostState() },
            )
        }
    }
}