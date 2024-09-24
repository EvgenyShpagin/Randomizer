package com.random.randomizer.presentation.core

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap

data class WheelItemUiState(
    val id: Int,
    val title: String,
    val description: String? = null,
    val image: ImageBitmap? = null,
    val customColor: Color? = null,
    val onClick: () -> Unit
)