package com.random.randomizer.presentation.core

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.ui.Modifier


fun Modifier.combinePaddingWithInsets(
    padding: PaddingValues,
    insets: WindowInsets
): Modifier {
    return this
        .padding(padding)
        .consumeWindowInsets(padding)
        .windowInsetsPadding(insets)
}