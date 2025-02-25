package com.random.randomizer.presentation.screen.edit

import com.random.randomizer.presentation.core.UiEffect

sealed interface EditUiEffect : UiEffect {
    data class ShowErrorMessage(val textId: Int) : EditUiEffect
    data object NavigateBack : EditUiEffect
}