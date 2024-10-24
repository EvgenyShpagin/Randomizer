package com.random.randomizer.presentation.core

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap

@Immutable
data class WheelSegmentUiState(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val image: ImageBitmap? = null,
    val customColor: Color? = null
)