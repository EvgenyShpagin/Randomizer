package com.random.randomizer.presentation.screen.results

import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.presentation.core.CoreMappers
import com.random.randomizer.presentation.core.WheelSegmentUiState
import javax.inject.Inject

interface ResultsMappers {
    fun toPresentation(wheelSegment: WheelSegment): WheelSegmentUiState
}

class ResultsMappersImpl @Inject constructor(
    private val coreMappers: CoreMappers
) : ResultsMappers {
    override fun toPresentation(wheelSegment: WheelSegment): WheelSegmentUiState {
        return coreMappers.toPresentation(wheelSegment)
    }
}