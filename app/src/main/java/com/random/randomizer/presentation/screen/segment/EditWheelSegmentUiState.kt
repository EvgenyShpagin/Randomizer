package com.random.randomizer.presentation.screen.segment

import androidx.compose.runtime.Immutable
import com.random.randomizer.presentation.core.UiState
import com.random.randomizer.presentation.core.WheelSegmentUiState

@Immutable
data class EditWheelSegmentUiState(
    val segmentUiState: WheelSegmentUiState = WheelSegmentUiState(),
    val canSave: Boolean = false
) : UiState