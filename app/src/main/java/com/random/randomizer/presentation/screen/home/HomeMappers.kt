package com.random.randomizer.presentation.screen.home

import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.presentation.core.CoreMappers
import com.random.randomizer.presentation.core.WheelSegmentUiState
import javax.inject.Inject

interface HomeMappers {
    fun toPresentation(wheelSegment: WheelSegment): WheelSegmentUiState
}

class HomeMappersImpl @Inject constructor(private val coreMappers: CoreMappers) : HomeMappers {
    override fun toPresentation(wheelSegment: WheelSegment): WheelSegmentUiState {
        return coreMappers.toPresentation(wheelSegment)
    }
}