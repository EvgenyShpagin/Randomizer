package com.random.randomizer.presentation.screen.edit

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
fun Modifier.bringIntoViewRequesterOnFocus(
    bringIntoViewRequester: BringIntoViewRequester,
    scope: CoroutineScope
): Modifier {
    return this
        .bringIntoViewRequester(bringIntoViewRequester)
        .onFocusChanged { state ->
            if (state.isFocused) {
                scope.launch {
                    bringIntoViewRequester.bringIntoView()
                }
            }
        }
}