package com.random.randomizer.presentation.screen.edit


import android.content.Context
import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.presentation.core.BaseViewModel
import kotlinx.coroutines.launch


class EditSegmentViewModel(
    private val wheelSegmentId: Int,
) : BaseViewModel<EditSegmentUiState, EditSegmentUiEvent, EditSegmentUiEffect>(
    initialUiState = EditSegmentUiState()
) {

    init {
        viewModelScope.launch {
            // TODO: subscribe to wheel segment changes
        }
    }

    private fun updateWheelSegment(
        onSuccess: () -> Unit = {},
        transform: (WheelSegment) -> WheelSegment
    ) {
        //TODO: update
    }

    override fun onEvent(event: EditSegmentUiEvent) {
        when (event) {
            is EditSegmentUiEvent.InputTitle -> onInputTitle(event.text)
            is EditSegmentUiEvent.InputDescription -> onInputDescription(event.text)
            is EditSegmentUiEvent.PickImage -> onPickImage(event.context, event.uri)
            is EditSegmentUiEvent.PickColor -> onPickBackgroundColor(event.color)
            EditSegmentUiEvent.RemoveImage -> onRemoveImage()
            EditSegmentUiEvent.OpenImagePicker -> onOpenImagePicker()
        }
    }

    private fun onInputTitle(title: String) {
        updateWheelSegment { it.copy(title = title) }
    }

    private fun onInputDescription(description: String) {
        updateWheelSegment { it.copy(description = description) }
    }

    private fun onPickImage(context: Context, imageUri: Uri?) {
        if (imageUri == null) return
        // TODO: save image thumbnail and update
    }

    private fun onRemoveImage() {
        updateWheelSegment(
            onSuccess = { /*TODO: delete image thumbnail*/ },
            transform = { it.copy(thumbnailPath = null) }
        )
    }

    private fun onPickBackgroundColor(color: Color?) {
        updateWheelSegment { it.copy(customColor = color?.value?.toLong()) }
    }

    private fun onOpenImagePicker() {
        triggerEffect(EditSegmentUiEffect.OpenImagePicker)
    }

    companion object {
        fun Factory(wheelSegmentId: Int) = viewModelFactory {
            initializer {
                EditSegmentViewModel(wheelSegmentId)
            }
        }
    }
}