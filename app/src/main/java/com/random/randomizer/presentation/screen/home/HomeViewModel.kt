package com.random.randomizer.presentation.screen.home

import androidx.lifecycle.viewModelScope
import com.random.randomizer.domain.usecase.DeleteWheelSegmentUseCase
import com.random.randomizer.domain.usecase.GetWheelSegmentsStreamUseCase
import com.random.randomizer.presentation.core.ImmutableStateViewModel
import com.random.randomizer.presentation.core.WheelSegmentUiState
import com.random.randomizer.presentation.core.toPresentation
import com.random.randomizer.presentation.screen.home.HomeUiEffect.NavigateToSegmentEdit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getWheelSegmentsStreamUseCase: GetWheelSegmentsStreamUseCase,
    private val deleteWheelSegmentUseCase: DeleteWheelSegmentUseCase
) : ImmutableStateViewModel<HomeUiState, HomeUiEvent, HomeUiEffect>() {

    override val uiState = getWheelSegmentsStreamUseCase()
        .map { wheelSegments ->
            HomeUiState(
                wheelSegments = wheelSegments.map { toPresentation(it) },
                isLoading = false
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiState()
        )

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