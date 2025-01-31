package com.random.randomizer.presentation.screen.edit

import com.random.randomizer.presentation.core.UiEffect

sealed interface EditUiEffect : UiEffect {
    data class NavigateToSegmentEdit(val segmentId: Int) : EditUiEffect
}