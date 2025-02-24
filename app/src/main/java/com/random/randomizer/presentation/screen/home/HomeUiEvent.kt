package com.random.randomizer.presentation.screen.home

import com.random.randomizer.presentation.core.UiEvent
import com.random.randomizer.presentation.core.WheelSegmentUiState

sealed interface HomeUiEvent : UiEvent {
    data object CreateSegment : HomeUiEvent
    data class EditSegment(val wheelSegment: WheelSegmentUiState) : HomeUiEvent
}