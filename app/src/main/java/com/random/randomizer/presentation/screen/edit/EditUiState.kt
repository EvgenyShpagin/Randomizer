package com.random.randomizer.presentation.screen.edit

import androidx.compose.runtime.Immutable
import com.random.randomizer.presentation.core.UiState
import com.random.randomizer.presentation.core.WheelSegmentUiState

@Immutable
data class EditUiState(
    val segmentUiState: WheelSegmentUiState = WheelSegmentUiState(),
    val canSave: Boolean = false
) : UiState