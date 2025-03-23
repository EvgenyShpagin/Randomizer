package com.random.randomizer.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import com.random.randomizer.presentation.core.UiEffect
import kotlinx.coroutines.flow.Flow

@Composable
fun <E : UiEffect> HandleUiEffects(flow: Flow<E>, onReceive: suspend (E) -> Unit) {
    val uiEffect = rememberFlowWithLifecycle(flow)
    LaunchedEffect(uiEffect) {
        uiEffect.collect { onReceive(it) }
    }
}

@Composable
private fun <T> rememberFlowWithLifecycle(
    flow: Flow<T>,
    lifecycle: Lifecycle = LocalLifecycleOwner.current.lifecycle
): Flow<T> {
    return remember(flow, lifecycle) {
        flow.flowWithLifecycle(
            lifecycle,
            Lifecycle.State.STARTED
        )
    }
}