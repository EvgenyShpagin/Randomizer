package com.random.randomizer.presentation.screen.edit

import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.presentation.core.FakeCoreMappers
import com.random.randomizer.presentation.core.WheelSegmentUiState

object FakeEditMappers : EditMappers {
    override fun toPresentation(wheelSegment: WheelSegment): WheelSegmentUiState {
        return FakeCoreMappers.toPresentation(wheelSegment)
    }

    override fun toDomain(
        wheelSegmentUiState: WheelSegmentUiState,
        thumbnailPath: String?
    ): WheelSegment {
        return FakeCoreMappers.toDomain(wheelSegmentUiState, thumbnailPath)
    }
}