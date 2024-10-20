package com.random.randomizer.presentation.core

interface Reducer<State : Reducer.UiState, Event : Reducer.UiEvent, Effect : Reducer.UiEffect> {
    interface UiState
    interface UiEvent
    interface UiEffect

    fun reduce(previousState: State, event: Event): Pair<State, Effect?>
}