package com.random.randomizer.presentation.screen.edit


import android.content.Context
import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import com.random.randomizer.R
import com.random.randomizer.domain.error.UpdateWheelSegmentError
import com.random.randomizer.domain.error.UpdateWheelSegmentError.FailedToSaveThumbnail
import com.random.randomizer.domain.error.UpdateWheelSegmentError.WheelSegmentDoesNotExist
import com.random.randomizer.domain.model.Image
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.usecase.GetWheelSegmentStreamUseCase
import com.random.randomizer.domain.usecase.UpdateWheelSegmentUseCase
import com.random.randomizer.presentation.core.MutableStateViewModel
import com.random.randomizer.presentation.core.WheelSegmentUiState
import com.random.randomizer.presentation.screen.edit.EditSegmentUiEffect.ShowErrorMessage
import com.random.randomizer.util.getUniqueFilename
import com.random.randomizer.util.toByteArray
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = EditSegmentViewModel.Factory::class)
class EditSegmentViewModel @AssistedInject constructor(
    @Assisted private val wheelSegmentId: Int,
    getWheelSegmentStreamUseCase: GetWheelSegmentStreamUseCase,
    private val updateWheelSegmentUseCase: UpdateWheelSegmentUseCase,
    private val mappers: EditSegmentMappers
) : MutableStateViewModel<WheelSegmentUiState, EditSegmentUiEvent, EditSegmentUiEffect>(
    initialUiState = WheelSegmentUiState()
) {

    init {
        getWheelSegmentStreamUseCase(wheelSegmentId)
            .onEach { segment -> handleWheelSegment(segment) }
            .launchIn(viewModelScope)
    }

    private fun handleWheelSegment(wheelSegment: WheelSegment) {
        updateState { mappers.toPresentation(wheelSegment) }
    }

    private fun updateWheelSegment(transform: (WheelSegment) -> WheelSegment) {
        viewModelScope.launch {
            updateWheelSegmentUseCase(wheelSegmentId, transform)
                .onFailure { handleUpdateError(it) }
        }
    }

    private fun handleUpdateError(error: UpdateWheelSegmentError) {
        when (error) {
            FailedToSaveThumbnail -> {
                triggerEffect(ShowErrorMessage(R.string.message_failed_to_set_image))
            }

            WheelSegmentDoesNotExist -> {
                // Do nothing - just wait
            }
        }

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

    private fun onPickImage(context: Context, imageUri: Uri?) = viewModelScope.launch {
        if (imageUri == null) return@launch

        val imageFilename = imageUri.getUniqueFilename()
        val imageData = imageUri.toByteArray(context)

        if (imageData == null) {
            triggerEffect(effect = ShowErrorMessage(R.string.message_failed_to_set_image))
            return@launch
        }

        updateWheelSegment {
            it.copy(thumbnail = Image(imageFilename, imageData))
        }
    }

    private fun onRemoveImage() {
        updateWheelSegment { it.copy(thumbnail = null) }
    }

    private fun onPickBackgroundColor(color: Color?) {
        val domainColor = color?.let { color -> mappers.toDomain(color) }
        updateWheelSegment { it.copy(customColor = domainColor) }
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