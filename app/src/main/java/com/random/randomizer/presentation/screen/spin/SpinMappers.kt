package com.random.randomizer.presentation.screen.spin

import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.presentation.core.CoreMappersImpl
import com.random.randomizer.presentation.core.WheelSegmentUiState

fun toPresentation(wheelSegment: WheelSegment): WheelSegmentUiState {
    return CoreMappersImpl().toPresentation(wheelSegment)
}