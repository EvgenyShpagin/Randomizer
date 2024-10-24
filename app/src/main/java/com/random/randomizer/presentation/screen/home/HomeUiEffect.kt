package com.random.randomizer.presentation.screen.home

import com.random.randomizer.presentation.core.BaseViewModel

sealed interface HomeUiEffect : BaseViewModel.UiEffect {
    data object NavigateToEdit : HomeUiEffect
    data object NavigateToSpin : HomeUiEffect
}