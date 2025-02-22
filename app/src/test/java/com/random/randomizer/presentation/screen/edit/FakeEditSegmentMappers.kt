package com.random.randomizer.presentation.screen.edit

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import com.random.randomizer.domain.model.Image
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.presentation.core.FakeCoreMappers
import com.random.randomizer.presentation.core.WheelSegmentUiState
import com.random.randomizer.presentation.screen.segment.EditWheelSegmentMappers

object FakeEditSegmentMappers : EditWheelSegmentMappers {
    override fun toPresentation(thumbnail: Image): ImageBitmap? {
        return FakeCoreMappers.toPresentation(thumbnail)
    }

    override fun toPresentation(color: Long): Color {
        return Color(color)
    }

    override fun toDomain(color: Color): Long {
        return FakeCoreMappers.toDomain(color)
    }

    override fun toDomain(
        wheelSegmentUiState: WheelSegmentUiState,
        thumbnail: Image?
    ): WheelSegment {
        return FakeCoreMappers.toDomain(wheelSegmentUiState, thumbnail)
    }
}