package com.random.randomizer.presentation.core

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import com.random.randomizer.domain.model.WheelSegment

fun WheelSegment.toUiState(): WheelSegmentUiState {
    return WheelSegmentUiState(
        id = id,
        title = title,
        description = description,
        image = thumbnailPath?.let { BitmapFactory.decodeFile(it) }?.asImageBitmap(),
        customColor = customColor?.let { Color(it) }
    )
}
