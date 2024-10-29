package com.random.randomizer.presentation.screen.edit

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.util.decodeFile

fun WheelSegment.toEditUiState(): EditSegmentUiState {
    return EditSegmentUiState(
        wheelSegmentId = id,
        title = title,
        description = description,
        thumbnail = thumbnailPath?.let { ImageBitmap.decodeFile(it) },
        checkedColor = customColor?.let { Color(it) }
    )
}