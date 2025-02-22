package com.random.randomizer.presentation.screen.segment


import android.content.Context
import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.random.randomizer.R
import com.random.randomizer.data.util.toBitmap
import com.random.randomizer.domain.common.Result
import com.random.randomizer.domain.error.CreateWheelSegmentError
import com.random.randomizer.domain.error.UpdateWheelSegmentError
import com.random.randomizer.domain.model.Image
import com.random.randomizer.domain.usecase.CreateWheelSegmentUseCase
import com.random.randomizer.domain.usecase.FixWheelSegmentUseCase
import com.random.randomizer.domain.usecase.GetWheelSegmentUseCase
import com.random.randomizer.domain.usecase.UpdateWheelSegmentUseCase
import com.random.randomizer.domain.usecase.ValidateWheelSegmentUseCase
import com.random.randomizer.presentation.core.ImmutableStateViewModel
import com.random.randomizer.presentation.core.WheelSegmentUiState
import com.random.randomizer.presentation.navigation.Destination
import com.random.randomizer.presentation.screen.segment.EditWheelSegmentUiEffect.ShowErrorMessage
import com.random.randomizer.util.getUniqueFilename
import com.random.randomizer.util.toByteArray
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class EditWheelSegmentViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getWheelSegmentUseCase: GetWheelSegmentUseCase,
    private val fixWheelSegmentUseCase: FixWheelSegmentUseCase,
    private val createWheelSegmentUseCase: CreateWheelSegmentUseCase,
    private val validateWheelSegmentUseCase: ValidateWheelSegmentUseCase,
    private val updateWheelSegmentUseCase: UpdateWheelSegmentUseCase,
    private val mappers: EditWheelSegmentMappers
) : ImmutableStateViewModel<EditWheelSegmentUiState, EditWheelSegmentUiEvent, EditWheelSegmentUiEffect>() {

    private val title = savedStateHandle.getStateFlow(KEY_TITLE, "")
    private val description = savedStateHandle.getStateFlow(KEY_DESCRIPTION, "")
    private val color = savedStateHandle.getStateFlow<Long?>(KEY_COLOR, null)
    private val imagePath = savedStateHandle.getStateFlow<String?>(KEY_IMAGE_PATH, null)
    private var latestImage: Image? = null

    private val route = savedStateHandle.toRoute<Destination.EditWheelSegment>()
    private val wheelSegmentId = route.wheelSegmentId
    private val isWheelSegmentCreated get() = wheelSegmentId != null

    override val uiState: StateFlow<EditWheelSegmentUiState> = combine(
        title, description, color, imagePath
    ) { title, description, color, imagePath ->
        if (latestImage?.id != imagePath) {
            latestImage = imagePath?.let { getImageFromPath(it) }
        }
        val wheelSegmentUiState = WheelSegmentUiState(
            id = wheelSegmentId ?: 0,
            title = title,
            description = description,
            image = latestImage?.toBitmap()?.asImageBitmap(),
            customColor = color?.let { mappers.toPresentation(it) }
        )
        val validateResult = validateWheelSegmentUseCase(
            mappers.toDomain(wheelSegmentUiState, latestImage)
        )
        EditWheelSegmentUiState(
            segmentUiState = wheelSegmentUiState,
            canSave = validateResult is Result.Success
        )
    }.onStart {
        initUiState()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        EditWheelSegmentUiState()
    )

    private fun getImageFromPath(path: String): Image {
        val imageFile = File(path)
        val imageData = imageFile.readBytes()
        return Image(path, imageData)
    }

    private suspend fun initUiState() {
        if (!isWheelSegmentCreated) return
        val savedWheelSegment = getWheelSegmentUseCase(wheelSegmentId!!)!!
        savedStateHandle[KEY_TITLE] = savedWheelSegment.title
        savedStateHandle[KEY_DESCRIPTION] = savedWheelSegment.description
        savedStateHandle[KEY_COLOR] = savedWheelSegment.customColor
        savedStateHandle[KEY_IMAGE_PATH] = savedWheelSegment.thumbnail?.id
    }

    override fun onEvent(event: EditWheelSegmentUiEvent) {
        when (event) {
            is EditWheelSegmentUiEvent.InputTitle -> onInputTitle(event.text)
            is EditWheelSegmentUiEvent.InputDescription -> onInputDescription(event.text)
            is EditWheelSegmentUiEvent.PickImage -> onPickImage(event.context, event.uri)
            is EditWheelSegmentUiEvent.PickColor -> onPickBackgroundColor(event.color)
            EditWheelSegmentUiEvent.RemoveImage -> onRemoveImage()
            is EditWheelSegmentUiEvent.FinishEdit -> onFinishEdit(doSave = event.doSave)
        }
    }

    private fun onInputTitle(title: String) {
        savedStateHandle[KEY_TITLE] = title
    }

    private fun onInputDescription(description: String) {
        savedStateHandle[KEY_DESCRIPTION] = description
    }

    private fun onPickImage(context: Context, imageUri: Uri?) = viewModelScope.launch {
        if (imageUri == null) return@launch

        val imageFilename = imageUri.getUniqueFilename()
        val imageData = imageUri.toByteArray(context)

        if (imageData == null) {
            triggerEffect(effect = ShowErrorMessage(R.string.message_failed_to_set_image))
            return@launch
        }

        cacheImage(context, Image(imageFilename, imageData))
    }

    private fun onRemoveImage() {
        deleteCachedImage()
        savedStateHandle[KEY_IMAGE_PATH] = null
    }

    private fun onPickBackgroundColor(color: Color?) {
        val domainColor = color?.let { mappers.toDomain(color) }
        savedStateHandle[KEY_COLOR] = domainColor
    }

    private fun onFinishEdit(doSave: Boolean) {
        viewModelScope.launch {
            if (doSave) {
                save()
            }
            deleteCachedImage()
            triggerEffect(EditWheelSegmentUiEffect.NavigateBack)
        }
    }

    private suspend fun save() {
        val wheelSegment = mappers.toDomain(
            wheelSegmentUiState = uiState.value.segmentUiState,
            thumbnail = latestImage
        )
        val fixedWheelSegment = fixWheelSegmentUseCase(wheelSegment)

        if (isWheelSegmentCreated) {
            updateWheelSegmentUseCase(fixedWheelSegment)
                .onFailure { handleError(it) }
        } else {
            createWheelSegmentUseCase(fixedWheelSegment)
                .onFailure { handleError(it) }
        }
    }

    private fun handleError(error: Error) {
        if (error is CreateWheelSegmentError.FailedToSaveThumbnail
            || error is UpdateWheelSegmentError.FailedToSaveThumbnail
        ) {
            triggerEffect(ShowErrorMessage(R.string.message_failed_to_set_image))
        }
    }

    private fun cacheImage(context: Context, image: Image) {
        val cacheDirectory = context.cacheDir
        val imageFile = File(cacheDirectory, image.id)
        runCatching {
            imageFile.writeBytes(image.data)
        }.onFailure {
            imageFile.delete()
        }.onSuccess {
            savedStateHandle[KEY_IMAGE_PATH] = imageFile.path
        }
    }

    private fun deleteCachedImage() {
        val imagePath = savedStateHandle.get<String>(KEY_IMAGE_PATH) ?: return
        val imageFile = File(imagePath)
        imageFile.delete()
    }

    private companion object {
        const val KEY_TITLE = "title"
        const val KEY_DESCRIPTION = "description"
        const val KEY_COLOR = "color"
        const val KEY_IMAGE_PATH = "image"
    }
}