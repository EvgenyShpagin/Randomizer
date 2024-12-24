package com.random.randomizer.presentation.screen.spin

import androidx.compose.runtime.Immutable
import com.random.randomizer.presentation.core.UiState
import com.random.randomizer.presentation.core.WheelSegmentUiState

@Immutable
data class SpinUiState(
    val isSpinning: Boolean = false,
    val wheelSegments: List<WheelSegmentUiState> = emptyList()
) : UiState