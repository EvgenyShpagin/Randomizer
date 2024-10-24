package com.random.randomizer.presentation.screen.edit

import com.random.randomizer.presentation.core.BaseViewModel
import com.random.randomizer.presentation.core.WheelSegmentUiState

class EditViewModel : BaseViewModel<EditUiState, EditUiEvent, EditUiEffect>(
    initialUiState = EditUiState()
) {
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
        // TODO: create segment and pass in it to state
    }

    private fun onNavigateBack() {
        triggerEffect(EditUiEffect.NavigateToHome)
    }
}