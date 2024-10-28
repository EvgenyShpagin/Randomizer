package com.random.randomizer.presentation.screen.edit


import android.content.Context
import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.usecase.GetWheelSegmentStreamUseCase
import com.random.randomizer.presentation.core.toUiState
import com.random.randomizer.presentation.core.BaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = EditSegmentViewModel.Factory::class)
class EditSegmentViewModel @AssistedInject constructor(
    @Assisted private val wheelSegmentId: Int,
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

    /**
     * Factory for passing in wheel segment being edited
     */
    @AssistedFactory
    interface Factory {
        fun create(wheelSegmentId: Int): EditSegmentViewModel
    }
}