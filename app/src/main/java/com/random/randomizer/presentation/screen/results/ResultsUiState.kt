package com.random.randomizer.presentation.screen.results

import com.random.randomizer.presentation.core.UiState
import com.random.randomizer.presentation.core.WheelSegmentUiState

data class ResultsUiState(
    val winnerUiState: WheelSegmentUiState = WheelSegmentUiState(),
    val canDeleteWinner: Boolean = false
) : UiState