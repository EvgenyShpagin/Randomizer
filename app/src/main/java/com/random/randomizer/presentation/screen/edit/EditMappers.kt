package com.random.randomizer.presentation.screen.edit

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import com.random.randomizer.domain.model.Image
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.presentation.core.CoreMappers
import com.random.randomizer.presentation.core.WheelSegmentUiState
import javax.inject.Inject

interface EditMappers {
    fun toPresentation(thumbnail: Image): ImageBitmap?
    fun toPresentation(color: Long): Color
    fun toDomain(color: Color): Long
    fun toDomain(
        wheelSegmentUiState: WheelSegmentUiState,
        thumbnail: Image?
    ): WheelSegment
}

class EditMappersImpl @Inject constructor(
    private val coreMappers: CoreMappers
) : EditMappers {
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