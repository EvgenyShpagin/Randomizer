package com.random.randomizer.presentation.screen.edit

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.presentation.core.CoreMappers
import com.random.randomizer.presentation.core.WheelSegmentUiState
import javax.inject.Inject

interface EditMappers {
    fun toPresentation(wheelSegment: WheelSegment): WheelSegmentUiState
    fun toDomain(wheelSegmentUiState: WheelSegmentUiState, thumbnailPath: String?): WheelSegment
}

interface EditSegmentMappers {
    fun toPresentation(wheelSegment: WheelSegment): EditSegmentUiState
    fun toPresentation(imagePath: String): ImageBitmap?
    fun toDomain(color: Color): Long
}

class EditMappersImpl @Inject constructor(private val coreMappers: CoreMappers) : EditMappers {
    override fun toPresentation(wheelSegment: WheelSegment): WheelSegmentUiState {
        return coreMappers.toPresentation(wheelSegment)
    }

    override fun toDomain(
        wheelSegmentUiState: WheelSegmentUiState,
        thumbnailPath: String?
    ): WheelSegment {
        return coreMappers.toDomain(wheelSegmentUiState, thumbnailPath)
    }
}

class EditSegmentMappersImpl @Inject constructor(
    private val coreMappers: CoreMappers
) : EditSegmentMappers {
    override fun toPresentation(wheelSegment: WheelSegment): EditSegmentUiState {
        return EditSegmentUiState(
            wheelSegmentId = wheelSegment.id,
            title = wheelSegment.title,
            description = wheelSegment.description,
            thumbnail = wheelSegment.thumbnailPath?.let { toPresentation(thumbnailPath = it) },
            checkedColor = wheelSegment.customColor?.let { Color(it) }
        )
    }

    override fun toPresentation(thumbnailPath: String): ImageBitmap? {
        return coreMappers.toPresentation(thumbnailPath)
    }

    override fun toDomain(color: Color): Long {
        return coreMappers.toDomain(color)
    }
}