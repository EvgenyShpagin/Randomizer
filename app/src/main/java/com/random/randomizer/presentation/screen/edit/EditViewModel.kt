package com.random.randomizer.presentation.screen.edit

import androidx.lifecycle.viewModelScope
import com.random.randomizer.domain.usecase.CreateWheelSegmentUseCase
import com.random.randomizer.domain.usecase.GetWheelSegmentsStreamUseCase
import com.random.randomizer.mapper.toPresentation
import com.random.randomizer.presentation.core.BaseViewModel
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
) : BaseViewModel<EditUiState, EditUiEvent, EditUiEffect>(
    initialUiState = EditUiState()
) {
    init {
        getWheelSegmentsStreamUseCase()
            .onEach { segments ->
                val uiSegments = segments.map { it.toPresentation() }
                updateState { it.copy(wheelSegments = uiSegments) }
            }
            .launchIn(viewModelScope)
    }

    override fun onEvent(event: EditUiEvent) {
        when (event) {
            is EditUiEvent.EditSegment -> onEditSegment(event.wheelSegment)
            EditUiEvent.CreateSegment -> onCreateSegment()
            EditUiEvent.NavigateBack -> onNavigateBack()
        }
    }

    private fun onEditSegment(wheelSegment: WheelSegmentUiState) {
        updateState { it.copy(currentlyEditedSegment = wheelSegment) }
    }

    private fun onCreateSegment() {
        viewModelScope.launch {
            val newWheelSegmentUiState = createWheelSegmentUseCase().toPresentation()
            updateState {
                it.copy(
                    wheelSegments = it.wheelSegments + newWheelSegmentUiState,
                    currentlyEditedSegment = newWheelSegmentUiState
                )
            }
        }
    }

    private fun onNavigateBack() {
        triggerEffect(EditUiEffect.NavigateToHome)
    }
}