package com.random.randomizer.presentation.screen.edit

import android.content.Context
import android.net.Uri
import androidx.compose.ui.graphics.Color
import com.random.randomizer.presentation.core.BaseViewModel

sealed interface EditSegmentUiEvent : BaseViewModel.UiEvent {
    data class InputTitle(val text: String) : EditSegmentUiEvent
    data class InputDescription(val text: String) : EditSegmentUiEvent
    data class PickImage(val context: Context, val uri: Uri?) : EditSegmentUiEvent
    data object OpenImagePicker : EditSegmentUiEvent
    data object RemoveImage : EditSegmentUiEvent
    data class PickColor(val color: Color?) : EditSegmentUiEvent
}