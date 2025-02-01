package com.random.randomizer.presentation.screen.edit

import androidx.lifecycle.viewModelScope
import com.random.randomizer.domain.usecase.DeleteWheelSegmentUseCase
import com.random.randomizer.domain.usecase.GetWheelSegmentsStreamUseCase
import com.random.randomizer.presentation.core.MutableStateViewModel
import com.random.randomizer.presentation.core.WheelSegmentUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(
    getWheelSegmentsStreamUseCase: GetWheelSegmentsStreamUseCase,
    private val deleteWheelSegmentUseCase: DeleteWheelSegmentUseCase,
    private val mappers: EditMappers
) : MutableStateViewModel<EditUiState, EditUiEvent, EditUiEffect>(
    initialUiState = EditUiState()
) {
    init {
        getWheelSegmentsStreamUseCase()
            .onEach { segments ->
                val uiSegments = segments.map { mappers.toPresentation(it) }
                updateState { it.copy(wheelSegments = uiSegments) }
            }
            .launchIn(viewModelScope)
    }

    override fun onEvent(event: EditUiEvent) {
        when (event) {
            is EditUiEvent.EditSegment -> onEditSegment(event.wheelSegment)
            EditUiEvent.CreateSegment -> onCreateSegment()
        }
    }

    private fun onEditSegment(wheelSegment: WheelSegmentUiState) {
        triggerEffect(EditUiEffect.NavigateToSegmentEdit(wheelSegment.id))
    }

    private fun onCreateSegment() {
        triggerEffect(EditUiEffect.NavigateToSegmentEdit(null))
    }
}