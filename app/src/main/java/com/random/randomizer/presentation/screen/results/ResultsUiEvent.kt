package com.random.randomizer.presentation.screen.results

import com.random.randomizer.presentation.core.UiEvent

sealed interface ResultsUiEvent : UiEvent {
    data object Spin : ResultsUiEvent
    data object SpinAndDelete : ResultsUiEvent
}