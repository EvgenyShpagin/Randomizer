package com.random.randomizer.presentation.core

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.random.randomizer.domain.model.Image
import com.random.randomizer.domain.model.WheelSegment

fun toPresentation(wheelSegment: WheelSegment): WheelSegmentUiState {
    return WheelSegmentUiState(
        id = wheelSegment.id,
        title = wheelSegment.title,
        description = wheelSegment.description,
        image = wheelSegment.thumbnail?.let { toPresentation(thumbnail = it) },
        customColor = wheelSegment.customColor?.let { toPresentation(it) }
    )
}

fun toPresentation(thumbnail: Image): ImageBitmap? {
    return BitmapFactory.decodeByteArray(thumbnail.data, 0, thumbnail.data.size)
        ?.asImageBitmap()
}

fun toDomain(
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

fun toDomain(color: Color): Long {
    return color.value.shr(32).toLong()
}

fun toPresentation(color: Long): Color {
    return Color(color)
}