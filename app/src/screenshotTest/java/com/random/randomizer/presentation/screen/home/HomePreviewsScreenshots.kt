package com.random.randomizer.presentation.screen.home

import android.content.res.Configuration
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.random.randomizer.presentation.core.WheelSegmentUiState
import com.random.randomizer.presentation.screen.util.MEDIUM_WINDOW_HEIGHT_BREAKPOINT
import com.random.randomizer.presentation.screen.util.ScreenshotPreviewContainer
import com.random.randomizer.presentation.util.MEDIUM_WINDOW_WIDTH_BREAKPOINT
import com.random.randomizer.presentation.util.PreviewWheelSegmentList
import com.random.randomizer.presentation.util.WheelSegmentListParameterProvider

@OptIn(
    ExperimentalSharedTransitionApi::class,
    ExperimentalMaterial3Api::class
)
class HomePreviewsScreenshots {

    @Preview
    @Composable
    private fun LightTheme_ParametrizedContentList_Preview(
        @PreviewParameter(WheelSegmentListParameterProvider::class)
        wheelSegments: List<WheelSegmentUiState>
    ) {
        HomeScreen(wheelSegments = wheelSegments)
    }

    @Preview
    @Composable
    private fun LightTheme_Loading_Preview() {
        HomeScreen(isLoading = true)
    }

    @Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
    @Composable
    private fun DarkTheme_ContentList_Preview() {
        HomeScreen(wheelSegments = PreviewWheelSegmentList)
    }

    @Preview(
        widthDp = MEDIUM_WINDOW_WIDTH_BREAKPOINT,
        heightDp = MEDIUM_WINDOW_HEIGHT_BREAKPOINT
    )
    @Composable
    private fun LightTheme_MediumWindow_ContentList_Preview() {
        HomeScreen(wheelSegments = PreviewWheelSegmentList)
    }

    @Preview(
        widthDp = MEDIUM_WINDOW_WIDTH_BREAKPOINT,
        heightDp = MEDIUM_WINDOW_HEIGHT_BREAKPOINT
    )
    @Composable
    private fun LightTheme_ContentGrid_Preview() {
        HomeScreen(
            wheelSegments = PreviewWheelSegmentList,
            showGrid = true
        )
    }

    @Composable
    private fun HomeScreen(
        modifier: Modifier = Modifier,
        wheelSegments: List<WheelSegmentUiState> = emptyList(),
        isLoading: Boolean = false,
        showGrid: Boolean = false
    ) {
        ScreenshotPreviewContainer { animatedVisibilityScope ->
            HomeScreen(
                animatedVisibilityScope = animatedVisibilityScope,
                modifier = modifier,
                onClickSpin = {},
                onClickAdd = {},
                onClickWheelSegment = {},
                onDeleteWheelSegment = {},
                wheelSegments = wheelSegments,
                isLoading = isLoading,
                showGrid = showGrid,
                showSettingsDialog = false,
                onShowSettings = {},
                onDismissSettings = {}
            )
        }
    }
}