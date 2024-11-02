package com.random.randomizer.presentation.core

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import com.random.randomizer.domain.model.WheelSegment

object FakeCoreMappers : CoreMappers {
    override fun toPresentation(wheelSegment: WheelSegment): WheelSegmentUiState {
        return WheelSegmentUiState(
            id = wheelSegment.id,
            title = wheelSegment.title,
            description = wheelSegment.description,
            image = wheelSegment.thumbnailPath?.let { toPresentation(thumbnailPath = it) },
            customColor = wheelSegment.customColor?.let { Color(it) }
        )
    }

    override fun toPresentation(thumbnailPath: String): ImageBitmap? {
        return null
    }

    override fun toDomain(
        wheelSegmentUiState: WheelSegmentUiState,
        thumbnailPath: String?
    ): WheelSegment {
        return WheelSegment(
            id = wheelSegmentUiState.id,
            title = wheelSegmentUiState.title,
            description = wheelSegmentUiState.description,
            thumbnailPath = thumbnailPath,
            customColor = wheelSegmentUiState.customColor?.let { toDomain(color = it) }
        )
    }

    override fun toDomain(color: Color): Long {
        return color.value.shr(32).toLong()
    }
}