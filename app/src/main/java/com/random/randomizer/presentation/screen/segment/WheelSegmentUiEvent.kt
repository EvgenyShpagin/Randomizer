package com.random.randomizer.presentation.screen.segment

import android.content.Context
import android.net.Uri
import androidx.compose.ui.graphics.Color
import com.random.randomizer.presentation.core.UiEvent

sealed interface WheelSegmentUiEvent : UiEvent {
    data class InputTitle(val text: String) : WheelSegmentUiEvent
    data class InputDescription(val text: String) : WheelSegmentUiEvent
    data class PickImage(val context: Context, val uri: Uri?) : WheelSegmentUiEvent
    data object OpenImagePicker : WheelSegmentUiEvent
    data object RemoveImage : WheelSegmentUiEvent
    data class FinishEdit(val doSave: Boolean) : WheelSegmentUiEvent
    data class PickColor(val color: Color?) : WheelSegmentUiEvent
}