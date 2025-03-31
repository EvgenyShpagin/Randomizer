package com.random.randomizer.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import com.random.randomizer.presentation.core.UiEffect
import kotlinx.coroutines.flow.Flow

@Composable
fun <E : UiEffect> HandleUiEffects(flow: Flow<E>, onReceive: suspend (E) -> Unit) {
    val uiEffect = rememberFlowWithLifecycle(flow)
    val currentOnReceive by rememberUpdatedState(onReceive)
    LaunchedEffect(uiEffect) {
        uiEffect.collect { currentOnReceive(it) }
    }
}