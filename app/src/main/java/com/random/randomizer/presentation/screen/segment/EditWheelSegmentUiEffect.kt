package com.random.randomizer.presentation.screen.segment

import com.random.randomizer.presentation.core.UiEffect

sealed interface EditWheelSegmentUiEffect : UiEffect {
    data object OpenImagePicker : EditWheelSegmentUiEffect
    data class ShowErrorMessage(val textId: Int) : EditWheelSegmentUiEffect
    data object NavigateBack : EditWheelSegmentUiEffect
}