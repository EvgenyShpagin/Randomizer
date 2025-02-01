package com.random.randomizer.presentation.screen.segment

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import com.random.randomizer.domain.model.Image
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.presentation.core.CoreMappers
import com.random.randomizer.presentation.core.WheelSegmentUiState
import javax.inject.Inject

interface WheelSegmentMappers {
    fun toPresentation(wheelSegment: WheelSegment): WheelSegmentUiState
    fun toPresentation(thumbnail: Image): ImageBitmap?
    fun toDomain(color: Color): Long
}

class WheelSegmentMappersImpl @Inject constructor(
    private val coreMappers: CoreMappers
) : WheelSegmentMappers {
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