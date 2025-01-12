package com.random.randomizer.presentation.screen.edit

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import com.random.randomizer.domain.model.Image
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.presentation.core.CoreMappers
import com.random.randomizer.presentation.core.WheelSegmentUiState
import javax.inject.Inject

interface EditMappers {
    fun toPresentation(wheelSegment: WheelSegment): WheelSegmentUiState
    fun toDomain(wheelSegmentUiState: WheelSegmentUiState, thumbnail: Image?): WheelSegment
}

interface EditSegmentMappers {
    fun toPresentation(wheelSegment: WheelSegment): WheelSegmentUiState
    fun toPresentation(thumbnail: Image): ImageBitmap?
    fun toDomain(color: Color): Long
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

class EditSegmentMappersImpl @Inject constructor(
    private val coreMappers: CoreMappers
) : EditSegmentMappers {
    override fun toPresentation(wheelSegment: WheelSegment): WheelSegmentUiState {
        return coreMappers.toPresentation(wheelSegment)
    }

    override fun toPresentation(thumbnail: Image): ImageBitmap? {
        return coreMappers.toPresentation(thumbnail)
    }

    override fun toDomain(color: Color): Long {
        return coreMappers.toDomain(color)
    }
}