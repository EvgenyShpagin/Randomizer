package com.random.randomizer.presentation.core

import androidx.lifecycle.ViewModel
import com.random.randomizer.presentation.core.BaseViewModel.UiEffect
import com.random.randomizer.presentation.core.BaseViewModel.UiEvent
import com.random.randomizer.presentation.core.BaseViewModel.UiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

abstract class BaseViewModel<State : UiState, Event : UiEvent, Effect : UiEffect>(
    initialUiState: State
) : ViewModel() {

    private val _uiState = MutableStateFlow(initialUiState)
    val uiState = _uiState.asStateFlow()

    private val _uiEffects = Channel<Effect>(capacity = Channel.CONFLATED)
    val uiEffect = _uiEffects.receiveAsFlow()

    abstract fun onEvent(event: Event)

    protected fun updateState(transform: (State) -> State) {
        _uiState.update { transform(it) }
    }

    protected fun triggerEffect(effect: Effect) {
        _uiEffects.trySend(effect)
    }

    interface UiState
    interface UiEvent
    interface UiEffect
}