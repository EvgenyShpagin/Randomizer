package com.random.randomizer.presentation.screen.home

import androidx.compose.runtime.Immutable
import com.random.randomizer.presentation.core.UiState
import com.random.randomizer.presentation.core.WheelSegmentUiState

@Immutable
data class HomeUiState(
    val wheelSegments: List<WheelSegmentUiState> = emptyList()
) : UiState