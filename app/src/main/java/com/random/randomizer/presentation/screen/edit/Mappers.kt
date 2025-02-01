package com.random.randomizer.presentation.screen.edit

import com.random.randomizer.domain.model.Image
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.presentation.core.CoreMappers
import com.random.randomizer.presentation.core.WheelSegmentUiState
import javax.inject.Inject

interface EditMappers {
    fun toPresentation(wheelSegment: WheelSegment): WheelSegmentUiState
    fun toDomain(wheelSegmentUiState: WheelSegmentUiState, thumbnail: Image?): WheelSegment
}

class EditMappersImpl @Inject constructor(private val coreMappers: CoreMappers) : EditMappers {
    override fun toPresentation(wheelSegment: WheelSegment): WheelSegmentUiState {
        return coreMappers.toPresentation(wheelSegment)
    }

    override fun toDomain(
        wheelSegmentUiState: WheelSegmentUiState,
        thumbnail: Image?
    ): WheelSegment {
        return coreMappers.toDomain(wheelSegmentUiState, thumbnail)
    }
}