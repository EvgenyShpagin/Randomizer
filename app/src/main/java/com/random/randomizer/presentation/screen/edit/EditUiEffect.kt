package com.random.randomizer.presentation.screen.edit

import com.random.randomizer.presentation.core.BaseViewModel

sealed interface EditUiEffect : BaseViewModel.UiEffect {
    data object NavigateToHome : EditUiEffect
}