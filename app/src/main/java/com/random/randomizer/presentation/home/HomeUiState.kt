package com.random.randomizer.presentation.home

import com.random.randomizer.presentation.core.WheelItemUiState

data class HomeUiState(
    val wheelItems: List<WheelItemUiState> = emptyList()
)