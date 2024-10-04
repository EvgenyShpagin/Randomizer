package com.random.randomizer.presentation.core

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap

@Immutable
data class WheelItemUiState(
    val id: Int,
    val title: String,
    val description: String? = null,
    val image: ImageBitmap? = null,
    val customColor: Color? = null,
    val onClick: () -> Unit
)