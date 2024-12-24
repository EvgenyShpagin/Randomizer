package com.random.randomizer.presentation.screen.edit

import androidx.compose.runtime.Immutable
import com.random.randomizer.presentation.core.UiState
import com.random.randomizer.presentation.core.WheelSegmentUiState

@Immutable
data class EditUiState(
    val wheelSegments: List<WheelSegmentUiState> = emptyList(),
    val currentlyEditedSegmentId: Int? = null
) : UiState

val EditUiState.currentlyEditedSegment
    get() = wheelSegments.find { it.id == currentlyEditedSegmentId }