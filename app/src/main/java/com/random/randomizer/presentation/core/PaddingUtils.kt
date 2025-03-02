package com.random.randomizer.presentation.core

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.coerceAtLeast


fun Modifier.combinePaddingWithInsets(
    padding: PaddingValues,
    insets: WindowInsets
): Modifier {
    return this
        .padding(padding)
        .consumeWindowInsets(padding)
        .windowInsetsPadding(insets)
}

@Composable
fun PaddingValues.unionWithWindowInsets(windowInsets: WindowInsets): PaddingValues {
    val layoutDirection = LocalLayoutDirection.current
    val windowInsetsPadding = windowInsets.asPaddingValues()
    return PaddingValues(
        start = windowInsetsPadding
            .calculateStartPadding(layoutDirection)
            .coerceAtLeast(calculateStartPadding(layoutDirection)),
        top = windowInsetsPadding
            .calculateTopPadding()
            .coerceAtLeast(calculateTopPadding()),
        end = windowInsetsPadding
            .calculateEndPadding(layoutDirection)
            .coerceAtLeast(calculateEndPadding(layoutDirection)),
        bottom = windowInsetsPadding
            .calculateBottomPadding()
            .coerceAtLeast(calculateBottomPadding())
    )
}