package com.random.randomizer.presentation.core

import androidx.lifecycle.ViewModel
import com.random.randomizer.presentation.core.Reducer.UiEffect
import com.random.randomizer.presentation.core.Reducer.UiEvent
import com.random.randomizer.presentation.core.Reducer.UiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

abstract class BaseViewModel<State : UiState, Event : UiEvent, Effect : UiEffect>(
    initialUiState: State,
    private val reducer: Reducer<State, Event, Effect>
) : ViewModel() {

    private val _state = MutableStateFlow(initialUiState)
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<Event>()
    val event = _event.asSharedFlow()

    private val _effects = Channel<Effect>(capacity = Channel.CONFLATED)
    val effect = _effects.receiveAsFlow()

    fun sendEffect(effect: Effect) {
        _effects.trySend(effect)
    }

    fun sendEvent(event: Event) {
        val (newState, _) = reducer.reduce(_state.value, event)
        _state.tryEmit(newState)
    }

    fun sendEventForEffect(event: Event) {
        val (newState, effect) = reducer.reduce(_state.value, event)
        _state.tryEmit(newState)
        effect?.let {
            sendEffect(it)
        }
    }
}