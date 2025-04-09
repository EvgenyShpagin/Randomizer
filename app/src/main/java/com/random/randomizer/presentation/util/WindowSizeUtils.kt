package com.random.randomizer.presentation.util

import androidx.annotation.VisibleForTesting
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass

fun WindowSizeClass.areSidesAtLeastMedium(): Boolean {
    return windowHeightSizeClass != WindowHeightSizeClass.COMPACT
            && windowWidthSizeClass != WindowWidthSizeClass.COMPACT
}

@VisibleForTesting
const val MEDIUM_WINDOW_WIDTH_BREAKPOINT = 600

val WindowWidthSizeClass.Companion.MediumBreakpoint
    get() = MEDIUM_WINDOW_WIDTH_BREAKPOINT.dp