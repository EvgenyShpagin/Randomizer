package com.random.randomizer.presentation.core

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import com.random.randomizer.domain.model.Image
import com.random.randomizer.domain.model.WheelSegment

object FakeCoreMappers : CoreMappers {
    override fun toPresentation(wheelSegment: WheelSegment): WheelSegmentUiState {
        return WheelSegmentUiState(
            id = wheelSegment.id,
            title = wheelSegment.title,
            description = wheelSegment.description,
            image = wheelSegment.thumbnail?.let { toPresentation(thumbnail = it) },
            customColor = wheelSegment.customColor?.let { toPresentation(it) }
        )
    }

    override fun toPresentation(thumbnail: Image): ImageBitmap? {
        return null
    }

    override fun toPresentation(color: Long): Color {
        return Color(color)
    }

    override fun toDomain(
        wheelSegmentUiState: WheelSegmentUiState,
        thumbnail: Image?
    ): WheelSegment {
        return WheelSegment(
            id = wheelSegmentUiState.id,
            title = wheelSegmentUiState.title,
            description = wheelSegmentUiState.description,
            thumbnail = thumbnail,
            customColor = wheelSegmentUiState.customColor?.let { toDomain(color = it) }
        )
    }

    override fun toDomain(color: Color): Long {
        return color.value.shr(32).toLong()
    }
}