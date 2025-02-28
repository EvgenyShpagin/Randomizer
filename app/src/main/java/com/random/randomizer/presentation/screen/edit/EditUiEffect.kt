package com.random.randomizer.presentation.screen.edit

import com.random.randomizer.presentation.core.UiEffect

sealed interface EditUiEffect : UiEffect {
    data object NavigateToHome : EditUiEffect
}