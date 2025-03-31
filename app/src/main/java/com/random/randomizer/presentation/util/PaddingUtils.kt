package com.random.randomizer.presentation.util

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.coerceAtLeast
import com.random.randomizer.presentation.theme.LocalBackground


fun Modifier.unionPaddingWithInsets(
    padding: PaddingValues,
    insets: WindowInsets
): Modifier {
    return this
        .padding(padding)
        .consumeWindowInsets(padding)
        .windowInsetsPadding(insets)
}

@Composable
fun PaddingValues.unionWithWindowInsets(
    windowInsets: WindowInsets,
    excludePadding: PaddingValues = LocalBackground.current.padding
): PaddingValues {
    val layoutDirection = LocalLayoutDirection.current
    val excludeWindowInsets = excludePadding.asWindowInsets(layoutDirection)
    val windowInsetsPadding = windowInsets
        .exclude(excludeWindowInsets)
        .asPaddingValues()
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

private fun PaddingValues.asWindowInsets(layoutDirection: LayoutDirection): WindowInsets {
    return WindowInsets(
        left = calculateLeftPadding(layoutDirection),
        top = calculateTopPadding(),
        right = calculateRightPadding(layoutDirection),
        bottom = calculateBottomPadding()
    )
}

@Composable
fun PaddingValues.add(other: PaddingValues): PaddingValues {
    return add(other, LocalLayoutDirection.current)
}

fun PaddingValues.add(
    other: PaddingValues,
    layoutDirection: LayoutDirection
): PaddingValues {
    return PaddingValues(
        start = calculateStartPadding(layoutDirection) + other.calculateStartPadding(layoutDirection),
        top = calculateTopPadding() + other.calculateTopPadding(),
        end = calculateEndPadding(layoutDirection) + other.calculateEndPadding(layoutDirection),
        bottom = calculateBottomPadding() + other.calculateBottomPadding()
    )
}