package com.random.randomizer.presentation.screen.spin

import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.presentation.core.CoreMappers
import com.random.randomizer.presentation.core.WheelSegmentUiState
import javax.inject.Inject

interface SpinMappers {
    fun toPresentation(wheelSegment: WheelSegment): WheelSegmentUiState
}

class SpinMappersImpl @Inject constructor(private val coreMappers: CoreMappers) : SpinMappers {
    override fun toPresentation(wheelSegment: WheelSegment): WheelSegmentUiState {
        return coreMappers.toPresentation(wheelSegment)
    }
}