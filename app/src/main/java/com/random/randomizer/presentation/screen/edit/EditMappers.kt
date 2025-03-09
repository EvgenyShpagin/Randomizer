package com.random.randomizer.presentation.screen.edit

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import com.random.randomizer.domain.model.Image
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.presentation.core.CoreMappersImpl
import com.random.randomizer.presentation.core.WheelSegmentUiState

fun toPresentation(thumbnail: Image): ImageBitmap? {
    return CoreMappersImpl().toPresentation(thumbnail)
}

fun toPresentation(color: Long): Color {
    return CoreMappersImpl().toPresentation(color)
}

fun toDomain(color: Color): Long {
    return CoreMappersImpl().toDomain(color)
}

fun toDomain(
    wheelSegmentUiState: WheelSegmentUiState,
    thumbnail: Image?
): WheelSegment {
    return CoreMappersImpl().toDomain(wheelSegmentUiState, thumbnail)
}