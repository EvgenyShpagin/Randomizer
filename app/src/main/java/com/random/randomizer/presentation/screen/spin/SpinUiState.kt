package com.random.randomizer.presentation.screen.spin

import androidx.compose.runtime.Immutable
import com.random.randomizer.presentation.core.BaseViewModel
import com.random.randomizer.presentation.core.WheelSegmentUiState

// TODO: remove fake
@Immutable
data class SpinUiState(
    val isSpinning: Boolean = false,
    val wheelSegments: List<WheelSegmentUiState> = emptyList()
) : BaseViewModel.UiState