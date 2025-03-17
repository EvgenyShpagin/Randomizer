package com.random.randomizer.presentation.util

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.random.randomizer.presentation.core.WheelSegmentUiState
import com.random.randomizer.presentation.theme.AppTheme

val PreviewWheelSegmentList: List<WheelSegmentUiState>
    get() = List(5) { i ->
        WheelSegmentUiState(
            id = i,
            title = "Title of segment ($i)",
            description = "Its ($i) description",
            customColor = if (i % 2 == 0) Color.Gray else null
        )
    }

class WheelSegmentListParameterProvider : PreviewParameterProvider<List<WheelSegmentUiState>> {
    override val values = sequenceOf(
        listOf(),
        PreviewWheelSegmentList
    )
}

/**
 * A composable container for previewing content
 * even within a [SharedTransitionLayout] and [AnimatedVisibility].
 *
 * This function provides a consistent environment for previewing composables
 * that utilize shared transitions and animations.
 *
 * @param content The composable content to be previewed.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun PreviewContainer(content: @Composable SharedTransitionScope.(AnimatedVisibilityScope) -> Unit) {
    SharedTransitionLayout {
        AnimatedVisibility(true) {
            AppTheme {
                Surface {
                    content(this)
                }
            }
        }
    }
}