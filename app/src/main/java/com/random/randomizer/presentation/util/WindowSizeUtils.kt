package com.random.randomizer.presentation.util

import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass

fun WindowSizeClass.areSidesAtLeastMedium(): Boolean {
    return windowHeightSizeClass != WindowHeightSizeClass.COMPACT
            && windowWidthSizeClass != WindowWidthSizeClass.COMPACT
}