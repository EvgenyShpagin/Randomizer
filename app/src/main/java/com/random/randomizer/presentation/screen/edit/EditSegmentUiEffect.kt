package com.random.randomizer.presentation.screen.edit

import com.random.randomizer.presentation.core.BaseViewModel

sealed interface EditSegmentUiEffect : BaseViewModel.UiEffect {
    data object OpenImagePicker : EditSegmentUiEffect
    data class ShowErrorMessage(val textId: Int) : EditSegmentUiEffect
}