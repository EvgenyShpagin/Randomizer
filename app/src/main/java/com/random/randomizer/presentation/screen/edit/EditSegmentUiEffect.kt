package com.random.randomizer.presentation.screen.edit

import com.random.randomizer.presentation.core.UiEffect

sealed interface EditSegmentUiEffect : UiEffect {
    data object OpenImagePicker : EditSegmentUiEffect
    data class ShowErrorMessage(val textId: Int) : EditSegmentUiEffect
}