package com.random.randomizer.presentation.screen.edit

import androidx.lifecycle.viewModelScope
import com.random.randomizer.domain.error.WheelSegmentValidationError.AlreadyExists
import com.random.randomizer.domain.error.WheelSegmentValidationError.Empty
import com.random.randomizer.domain.model.Image
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.usecase.CreateWheelSegmentUseCase
import com.random.randomizer.domain.usecase.DeleteWheelSegmentUseCase
import com.random.randomizer.domain.usecase.FixSavedWheelSegmentUseCase
import com.random.randomizer.domain.usecase.GetWheelSegmentsStreamUseCase
import com.random.randomizer.domain.usecase.MakeWheelSegmentUniqueUseCase
import com.random.randomizer.domain.usecase.ValidateWheelSegmentUseCase
import com.random.randomizer.presentation.core.MutableStateViewModel
import com.random.randomizer.presentation.core.WheelSegmentUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(
    getWheelSegmentsStreamUseCase: GetWheelSegmentsStreamUseCase,
    private val createWheelSegmentUseCase: CreateWheelSegmentUseCase,
    private val fixSavedWheelSegmentUseCase: FixSavedWheelSegmentUseCase,
    private val deleteWheelSegmentUseCase: DeleteWheelSegmentUseCase,
    private val validateWheelSegmentUseCase: ValidateWheelSegmentUseCase,
    private val makeWheelSegmentUniqueUseCase: MakeWheelSegmentUniqueUseCase,
    private val mappers: EditMappers
) : MutableStateViewModel<EditUiState, EditUiEvent, EditUiEffect>(
    initialUiState = EditUiState()
) {

    // Wheel segment's thumbnail required for mapping to domain entity
    private var currentlyEditedSegmentThumbnail: Image? = null

    init {
        getWheelSegmentsStreamUseCase()
            .onEach { segments ->
                val uiSegments = segments.map { mappers.toPresentation(it) }
                updateState { it.copy(wheelSegments = uiSegments) }
                updateCurrentlyEditedSegment(segments)
            }
            .launchIn(viewModelScope)
    }

    override fun onEvent(event: EditUiEvent) {
        when (event) {
            is EditUiEvent.EditSegment -> onEditSegment(event.wheelSegment)
            EditUiEvent.CreateSegment -> onCreateSegment()
            EditUiEvent.NavigateBack -> onNavigateBack()
            EditUiEvent.FinishSegmentEdit -> onFinishSegmentEdit()
        }
    }

    private fun onEditSegment(wheelSegment: WheelSegmentUiState) {
        updateState { it.copy(currentlyEditedSegmentId = wheelSegment.id) }
    }

    private fun onCreateSegment() {
        viewModelScope.launch {
            val newWheelSegmentUiState = mappers.toPresentation(createWheelSegmentUseCase())
            updateState {
                it.copy(
                    wheelSegments = it.wheelSegments + newWheelSegmentUiState,
                    currentlyEditedSegmentId = newWheelSegmentUiState.id
                )
            }
        }
    }

    private fun onNavigateBack() {
        triggerEffect(EditUiEffect.NavigateToHome)
    }

    private fun onFinishSegmentEdit() {
        val editedSegment = uiState.value.currentlyEditedSegment?.let { uiState ->
            mappers.toDomain(uiState, currentlyEditedSegmentThumbnail)
        } ?: return

        updateState { it.copy(currentlyEditedSegmentId = null) }
        currentlyEditedSegmentThumbnail = null

        viewModelScope.launch {
            fixSavedWheelSegmentUseCase(editedSegment.id)
            validateWheelSegmentUseCase(editedSegment)
                .onFailure { error ->
                    when (error) {
                        AlreadyExists -> makeWheelSegmentUniqueUseCase(editedSegment.id)
                        Empty -> deleteWheelSegmentUseCase(editedSegment.id)
                    }
                }
        }
    }

    private fun updateCurrentlyEditedSegment(domainWheelSegments: List<WheelSegment>) {
        val currentlyEditedSegmentId = uiState.value.currentlyEditedSegmentId ?: return
        currentlyEditedSegmentThumbnail = domainWheelSegments
            .find { it.id == currentlyEditedSegmentId }!!
            .thumbnail
    }
}