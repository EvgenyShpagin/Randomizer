package com.random.randomizer.presentation.screen.home

import androidx.compose.runtime.Immutable
import com.random.randomizer.presentation.core.WheelItemUiState

@Immutable
data class HomeUiState(
    val wheelItems: List<WheelItemUiState> = emptyList()
)