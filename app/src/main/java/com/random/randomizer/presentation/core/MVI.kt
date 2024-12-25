package com.random.randomizer.presentation.core

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

/**
 * Represents the state of the UI in the MVI pattern.
 *
 * Each implementation should define the specific properties
 * needed to render the UI. The state is immutable to ensure
 * a predictable and testable flow of changes.
 */
interface UiState

/**
 * Represents user-driven events in the MVI pattern.
 *
 * These events correspond to user actions or interactions,
 * such as button clicks or input changes. Each implementation
 * defines the actions relevant to the specific screen or feature.
 */
interface UiEvent

/**
 * Represents one-time effects in the MVI pattern.
 *
 * Effects are used for actions that do not affect the state
 * but require a one-time operation, such as navigation,
 * showing a toast, or logging an analytics event.
 */
interface UiEffect

/**
 * An abstract ViewModel for managing UI state and handling events in the MVI pattern.
 *
 * @param State The type representing the current state of the UI.
 * @param Event The type representing one-time events.
 * @param Effect The type representing one-time effects.
 */
abstract class ImmutableStateViewModel<
        State : UiState,
        Event : UiEvent,
        Effect : UiEffect> : ViewModel() {

    abstract val uiState: StateFlow<State>

    private val _uiEffects = Channel<Effect>(capacity = Channel.Factory.CONFLATED)
    val uiEffect = _uiEffects.receiveAsFlow()

    abstract fun onEvent(event: Event)

    protected fun triggerEffect(effect: Effect) {
        _uiEffects.trySend(effect)
    }
}

/**
 * An abstract ViewModel for managing UI state and handling events in the MVI pattern.
 * This ViewModel provides additional functionality for managing mutable state.
 *
 * The [uiState] property is now connected to the mutable [_uiState] property, so inheritor shouldn't override it
 *
 * @param State The type representing the current state of the UI.
 * @param Event The type representing one-time events.
 * @param Effect The type representing one-time effects.
 * @param initialUiState The initial state of the UI.
 */
abstract class MutableStateViewModel<State : UiState, Event : UiEvent, Effect : UiEffect>(
    initialUiState: State
) : ImmutableStateViewModel<State, Event, Effect>() {

    private val _uiState = MutableStateFlow(initialUiState)

    @get:CallSuper
    override val uiState = _uiState.asStateFlow()

    protected fun updateState(transform: (State) -> State) {
        _uiState.update { transform(it) }
    }
}