package com.random.randomizer.presentation.screen.segment

import com.random.randomizer.presentation.core.UiEffect

sealed interface WheelSegmentUiEffect : UiEffect {
    data object OpenImagePicker : WheelSegmentUiEffect
    data class ShowErrorMessage(val textId: Int) : WheelSegmentUiEffect
    data object NavigateBack : WheelSegmentUiEffect
}