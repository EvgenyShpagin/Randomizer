package com.random.randomizer.presentation.screen.edit

import com.random.randomizer.presentation.core.UiEvent
import com.random.randomizer.presentation.core.WheelSegmentUiState

sealed interface EditUiEvent : UiEvent {
    data object CreateSegment : EditUiEvent
    data class EditSegment(val wheelSegment: WheelSegmentUiState) : EditUiEvent
}