package com.random.randomizer.presentation.core

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.util.decodeFile

fun WheelSegment.toUiState(): WheelSegmentUiState {
    return WheelSegmentUiState(
        id = id,
        title = title,
        description = description,
        image = thumbnailPath?.let { ImageBitmap.decodeFile(it) },
        customColor = customColor?.let { Color(it) }
    )
}

fun WheelSegmentUiState.toDomain(thumbnailPath: String?): WheelSegment {
    return WheelSegment(
        id = id,
        title = title,
        description = description,
        thumbnailPath = thumbnailPath,
        customColor = customColor?.value?.toLong()
    )
}
