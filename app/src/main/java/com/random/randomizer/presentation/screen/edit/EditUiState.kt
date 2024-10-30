package com.random.randomizer.presentation.screen.edit

import androidx.compose.runtime.Immutable
import com.random.randomizer.presentation.core.BaseViewModel
import com.random.randomizer.presentation.core.WheelSegmentUiState

@Immutable
data class EditUiState(
    val wheelSegments: List<WheelSegmentUiState> = emptyList(),
    val currentlyEditedSegmentId: Int? = null
) : BaseViewModel.UiState