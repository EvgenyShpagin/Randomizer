package com.random.randomizer.presentation.screen.home

import com.random.randomizer.presentation.core.UiEffect

sealed interface HomeUiEffect : UiEffect {
    data object NavigateToEdit : HomeUiEffect
    data object NavigateToSpin : HomeUiEffect
}