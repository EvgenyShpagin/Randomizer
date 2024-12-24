package com.random.randomizer.presentation.screen.home

import com.random.randomizer.presentation.core.UiEvent

sealed interface HomeUiEvent : UiEvent {
    data object Edit : HomeUiEvent
    data object Spin : HomeUiEvent
}