package com.random.randomizer.presentation.screen.spin

import com.random.randomizer.presentation.core.UiEffect

sealed interface SpinUiEffect : UiEffect {
    data class NavigateToResults(val winnerId: Int) : SpinUiEffect
}