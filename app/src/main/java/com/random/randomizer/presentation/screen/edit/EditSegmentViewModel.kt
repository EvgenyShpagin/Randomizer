package com.random.randomizer.presentation.screen.edit


import android.content.Context
import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import com.random.randomizer.R
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.usecase.DeleteThumbnailUseCase
import com.random.randomizer.domain.usecase.GetWheelSegmentStreamUseCase
import com.random.randomizer.domain.usecase.SaveImageThumbnailUseCase
import com.random.randomizer.domain.usecase.UpdateWheelSegmentUseCase
import com.random.randomizer.presentation.core.BaseViewModel
import com.random.randomizer.presentation.core.WheelSegmentUiState
import com.random.randomizer.presentation.screen.edit.EditSegmentUiEffect.ShowErrorMessage
import com.random.randomizer.util.getInputStreamOrNull
import com.random.randomizer.util.getUniqueFilename
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
    private val saveImageThumbnailUseCase: SaveImageThumbnailUseCase,
    private val deleteThumbnailUseCase: DeleteThumbnailUseCase,
    private val mappers: EditSegmentMappers
) : BaseViewModel<WheelSegmentUiState, EditSegmentUiEvent, EditSegmentUiEffect>(
    initialUiState = WheelSegmentUiState()
) {

    init {
        getWheelSegmentStreamUseCase(wheelSegmentId)
            .onEach { segment -> updateState { mappers.toPresentation(segment) } }
            .launchIn(viewModelScope)
    }

    private fun updateWheelSegment(
        onSuccess: () -> Unit = {},
        transform: (WheelSegment) -> WheelSegment
    ) {
        viewModelScope.launch {
            updateWheelSegmentUseCase(wheelSegmentId, transform)
            onSuccess()
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

        val imageInputStream = imageUri.getInputStreamOrNull(context)
        if (imageInputStream == null) {
            triggerEffect(effect = ShowErrorMessage(R.string.message_failed_to_set_image))
            return@launch
        }

        saveImageThumbnailUseCase(
            imageId = imageUri.getUniqueFilename(),
            inputStream = imageInputStream
        ).onSuccess { path ->
            updateWheelSegment { it.copy(thumbnailPath = path) }
        }.onFailure {
            triggerEffect(effect = ShowErrorMessage(R.string.message_failed_to_set_image))
        }
    }

    private fun onRemoveImage() {
        updateWheelSegment(
            onSuccess = { viewModelScope.launch { deleteThumbnailUseCase() } },
            transform = { it.copy(thumbnailPath = null) }
        )
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