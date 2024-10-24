package com.random.randomizer.presentation.screen.home

import com.random.randomizer.presentation.core.BaseViewModel

sealed interface HomeUiEvent : BaseViewModel.UiEvent {
    data object Edit : HomeUiEvent
    data object Spin : HomeUiEvent
}