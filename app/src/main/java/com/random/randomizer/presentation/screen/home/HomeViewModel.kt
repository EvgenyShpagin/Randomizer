package com.random.randomizer.presentation.screen.home

import androidx.lifecycle.viewModelScope
import com.random.randomizer.domain.usecase.DeleteWheelSegmentUseCase
import com.random.randomizer.domain.usecase.GetWheelSegmentsStreamUseCase
import com.random.randomizer.presentation.core.MutableStateViewModel
import com.random.randomizer.presentation.core.WheelSegmentUiState
import com.random.randomizer.presentation.screen.home.HomeUiEffect.NavigateToSegmentEdit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getWheelSegmentsStreamUseCase: GetWheelSegmentsStreamUseCase,
    private val deleteWheelSegmentUseCase: DeleteWheelSegmentUseCase
) : MutableStateViewModel<HomeUiState, HomeUiEvent, HomeUiEffect>(
    initialUiState = HomeUiState()
) {

    init {
        getWheelSegmentsStreamUseCase()
            .onEach { segments ->
                val uiSegments = segments.map { toPresentation(it) }
                updateState { it.copy(wheelSegments = uiSegments) }
            }
            .launchIn(viewModelScope)
    }

    override fun onEvent(event: HomeUiEvent) {
        when (event) {
            HomeUiEvent.CreateSegment -> onCreateSegment()
            is HomeUiEvent.EditSegment -> onEditSegment(event.wheelSegment)
            is HomeUiEvent.DeleteSegment -> onDeleteSegment(event.wheelSegment)
        }
    }

    private fun onEditSegment(wheelSegment: WheelSegmentUiState) {
        triggerEffect(NavigateToSegmentEdit(wheelSegment.id))
    }

    private fun onCreateSegment() {
        triggerEffect(NavigateToSegmentEdit(null))
    }

    private fun onDeleteSegment(wheelSegment: WheelSegmentUiState) {
        viewModelScope.launch {
            deleteWheelSegmentUseCase(wheelSegment.id)
        }
    }
}