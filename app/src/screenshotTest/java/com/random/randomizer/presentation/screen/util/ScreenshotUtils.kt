package com.random.randomizer.presentation.screen.util

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import com.random.randomizer.presentation.core.RandomizerBackground
import com.random.randomizer.presentation.util.PreviewContainer

/**
 * A composable container for previewing screenshot content
 * even within a [SharedTransitionLayout] and [AnimatedVisibility].
 *
 * This function provides a consistent environment for previewing
 * screen-level composables that utilize shared transitions and animations.
 *
 * @param content The screen composable content to be previewed.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ScreenshotPreviewContainer(
    content: @Composable SharedTransitionScope.(animatedVisibilityScope: AnimatedVisibilityScope) -> Unit
) {
    PreviewContainer { animatedVisibilityScope ->
        RandomizerBackground {
            content(animatedVisibilityScope)
        }
    }
}


const val MEDIUM_WINDOW_HEIGHT_BREAKPOINT = 480