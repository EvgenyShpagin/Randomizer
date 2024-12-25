package com.random.randomizer.presentation.screen.spin

import com.random.randomizer.presentation.core.UiEvent

sealed interface SpinUiEvent : UiEvent {
    data object SpinFinished : SpinUiEvent
}