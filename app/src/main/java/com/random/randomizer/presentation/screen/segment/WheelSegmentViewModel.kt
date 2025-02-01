package com.random.randomizer.presentation.screen.segment


import android.content.Context
import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.random.randomizer.R
import com.random.randomizer.domain.error.UpdateWheelSegmentError
import com.random.randomizer.domain.error.UpdateWheelSegmentError.FailedToSaveThumbnail
import com.random.randomizer.domain.error.UpdateWheelSegmentError.WheelSegmentDoesNotExist
import com.random.randomizer.domain.model.Image
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.usecase.DeleteWheelSegmentUseCase
import com.random.randomizer.domain.usecase.FixSavedWheelSegmentUseCase
import com.random.randomizer.domain.usecase.GetWheelSegmentStreamUseCase
import com.random.randomizer.domain.usecase.UpdateWheelSegmentUseCase
import com.random.randomizer.domain.usecase.ValidateWheelSegmentUseCase
import com.random.randomizer.presentation.core.MutableStateViewModel
import com.random.randomizer.presentation.core.WheelSegmentUiState
import com.random.randomizer.presentation.navigation.Destination
import com.random.randomizer.presentation.screen.edit.EditSegmentMappers
import com.random.randomizer.util.getUniqueFilename
import com.random.randomizer.util.toByteArray
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WheelSegmentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getWheelSegmentStreamUseCase: GetWheelSegmentStreamUseCase,
    private val fixSavedWheelSegmentUseCase: FixSavedWheelSegmentUseCase,
    private val deleteWheelSegmentUseCase: DeleteWheelSegmentUseCase,
    private val validateWheelSegmentUseCase: ValidateWheelSegmentUseCase,
    private val updateWheelSegmentUseCase: UpdateWheelSegmentUseCase,
    private val mappers: EditSegmentMappers
) : MutableStateViewModel<WheelSegmentUiState, WheelSegmentUiEvent, WheelSegmentUiEffect>(
    initialUiState = WheelSegmentUiState()
) {

    private val route = savedStateHandle.toRoute<Destination.WheelSegment>()
    private val wheelSegmentId = route.wheelSegmentId
    private val isWheelSegmentCreated get() = wheelSegmentId != null

    init {
        if (isWheelSegmentCreated) {
            getWheelSegmentStreamUseCase(wheelSegmentId!!)
                .onEach { segment -> handleWheelSegment(segment) }
                .launchIn(viewModelScope)
        }
    }

    private fun handleWheelSegment(wheelSegment: WheelSegment) {
        updateState { mappers.toPresentation(wheelSegment) }
    }

    private fun updateWheelSegment(transform: (WheelSegment) -> WheelSegment) {
        if (!isWheelSegmentCreated) return
        viewModelScope.launch {
            updateWheelSegmentUseCase(wheelSegmentId!!, transform)
                .onFailure { handleUpdateError(it) }
        }
    }

    private fun handleUpdateError(error: UpdateWheelSegmentError) {
        when (error) {
            FailedToSaveThumbnail -> {
                triggerEffect(WheelSegmentUiEffect.ShowErrorMessage(R.string.message_failed_to_set_image))
            }

            WheelSegmentDoesNotExist -> {
                // Do nothing - just wait
            }
        }

    }

    override fun onEvent(event: WheelSegmentUiEvent) {
        when (event) {
            is WheelSegmentUiEvent.InputTitle -> onInputTitle(event.text)
            is WheelSegmentUiEvent.InputDescription -> onInputDescription(event.text)
            is WheelSegmentUiEvent.PickImage -> onPickImage(event.context, event.uri)
            is WheelSegmentUiEvent.PickColor -> onPickBackgroundColor(event.color)
            WheelSegmentUiEvent.RemoveImage -> onRemoveImage()
            WheelSegmentUiEvent.OpenImagePicker -> onOpenImagePicker()
            WheelSegmentUiEvent.FinishEdit -> onFinishEdit()
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
            triggerEffect(effect = WheelSegmentUiEffect.ShowErrorMessage(R.string.message_failed_to_set_image))
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
        triggerEffect(WheelSegmentUiEffect.OpenImagePicker)
    }

    private fun onFinishEdit() {
        // TODO: implement
        triggerEffect(WheelSegmentUiEffect.NavigateBack)
    }
}