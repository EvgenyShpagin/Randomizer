package com.random.randomizer.presentation.screen.edit

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.presentation.core.FakeCoreMappers

object FakeEditSegmentMappers : EditSegmentMappers {
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
        return FakeCoreMappers.toPresentation(thumbnailPath)
    }

    override fun toDomain(color: Color): Long {
        return FakeCoreMappers.toDomain(color)
    }
}