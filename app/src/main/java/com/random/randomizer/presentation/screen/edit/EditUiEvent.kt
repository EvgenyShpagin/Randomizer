package com.random.randomizer.presentation.screen.edit

import android.content.Context
import android.net.Uri
import androidx.compose.ui.graphics.Color
import com.random.randomizer.presentation.core.UiEvent

sealed interface EditUiEvent : UiEvent {
    data class InputTitle(val text: String) : EditUiEvent
    data class InputDescription(val text: String) : EditUiEvent
    data class PickImage(val context: Context, val uri: Uri?) : EditUiEvent
    data object RemoveImage : EditUiEvent
    data class FinishEdit(val doSave: Boolean) : EditUiEvent
    data class PickColor(val color: Color?) : EditUiEvent
}