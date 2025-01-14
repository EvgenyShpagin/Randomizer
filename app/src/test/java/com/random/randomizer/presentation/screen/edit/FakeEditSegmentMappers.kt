package com.random.randomizer.presentation.screen.edit

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import com.random.randomizer.domain.model.Image
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.presentation.core.FakeCoreMappers
import com.random.randomizer.presentation.core.WheelSegmentUiState

object FakeEditSegmentMappers : EditSegmentMappers {
    override fun toPresentation(wheelSegment: WheelSegment): WheelSegmentUiState {
        return FakeCoreMappers.toPresentation(wheelSegment)
    }

    override fun toPresentation(thumbnail: Image): ImageBitmap? {
        return FakeCoreMappers.toPresentation(thumbnail)
    }

    override fun toDomain(color: Color): Long {
        return FakeCoreMappers.toDomain(color)
    }
}