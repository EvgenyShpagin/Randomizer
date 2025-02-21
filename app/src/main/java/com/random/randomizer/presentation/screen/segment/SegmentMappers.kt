package com.random.randomizer.presentation.screen.segment

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import com.random.randomizer.domain.model.Image
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.presentation.core.CoreMappers
import com.random.randomizer.presentation.core.WheelSegmentUiState
import javax.inject.Inject

interface EditWheelSegmentMappers {
    fun toPresentation(thumbnail: Image): ImageBitmap?
    fun toPresentation(color: Long): Color
    fun toDomain(color: Color): Long
    fun toDomain(
        wheelSegmentUiState: WheelSegmentUiState,
        thumbnail: Image?
    ): WheelSegment
}

class EditWheelSegmentMappersImpl @Inject constructor(
    private val coreMappers: CoreMappers
) : EditWheelSegmentMappers {
    override fun toPresentation(thumbnail: Image): ImageBitmap? {
        return coreMappers.toPresentation(thumbnail)
    }

    override fun toPresentation(color: Long): Color {
        return Color(color)
    }

    override fun toDomain(color: Color): Long {
        return coreMappers.toDomain(color)
    }

    override fun toDomain(
        wheelSegmentUiState: WheelSegmentUiState,
        thumbnail: Image?
    ): WheelSegment {
        return coreMappers.toDomain(wheelSegmentUiState, thumbnail)
    }
}