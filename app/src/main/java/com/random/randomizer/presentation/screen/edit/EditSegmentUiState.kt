package com.random.randomizer.presentation.screen.edit

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import com.random.randomizer.presentation.core.BaseViewModel

@Immutable
data class EditSegmentUiState(
    val wheelSegmentId: Int = 0,
    val title: String = "",
    val description: String = "",
    val thumbnail: ImageBitmap? = null,
    val checkedColor: Color? = null
) : BaseViewModel.UiState {
    val canBeClosed get() = title.isNotEmpty()
}