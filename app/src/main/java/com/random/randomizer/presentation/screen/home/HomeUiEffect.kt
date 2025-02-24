package com.random.randomizer.presentation.screen.home

import com.random.randomizer.presentation.core.UiEffect

sealed interface HomeUiEffect : UiEffect {
    data class NavigateToSegmentEdit(val segmentId: Int?) : HomeUiEffect
}