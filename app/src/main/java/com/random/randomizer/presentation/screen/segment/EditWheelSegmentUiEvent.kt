package com.random.randomizer.presentation.screen.segment

import android.content.Context
import android.net.Uri
import androidx.compose.ui.graphics.Color
import com.random.randomizer.presentation.core.UiEvent

sealed interface EditWheelSegmentUiEvent : UiEvent {
    data class InputTitle(val text: String) : EditWheelSegmentUiEvent
    data class InputDescription(val text: String) : EditWheelSegmentUiEvent
    data class PickImage(val context: Context, val uri: Uri?) : EditWheelSegmentUiEvent
    data object OpenImagePicker : EditWheelSegmentUiEvent
    data object RemoveImage : EditWheelSegmentUiEvent
    data class FinishEdit(val doSave: Boolean) : EditWheelSegmentUiEvent
    data class PickColor(val color: Color?) : EditWheelSegmentUiEvent
}