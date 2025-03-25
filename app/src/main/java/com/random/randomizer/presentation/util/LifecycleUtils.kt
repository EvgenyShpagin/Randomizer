package com.random.randomizer.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.Flow

@Composable
fun <T> rememberFlowWithLifecycle(
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