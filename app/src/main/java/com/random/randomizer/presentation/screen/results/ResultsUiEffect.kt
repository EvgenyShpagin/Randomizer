package com.random.randomizer.presentation.screen.results

import com.random.randomizer.presentation.core.UiEffect

sealed interface ResultsUiEffect : UiEffect {
    data object NavigateToSpin : ResultsUiEffect
}