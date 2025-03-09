package com.random.randomizer.presentation.screen.results

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.random.randomizer.domain.usecase.DeleteWheelSegmentUseCase
import com.random.randomizer.domain.usecase.GetWheelSegmentStreamUseCase
import com.random.randomizer.domain.usecase.GetWheelSegmentsUseCase
import com.random.randomizer.presentation.core.ImmutableStateViewModel
import com.random.randomizer.presentation.core.toPresentation
import com.random.randomizer.presentation.navigation.Destination
import com.random.randomizer.presentation.screen.results.ResultsUiEffect.NavigateToSpin
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getWheelSegmentStreamUseCase: GetWheelSegmentStreamUseCase,
    getWheelSegmentsUseCase: GetWheelSegmentsUseCase,
    private val deleteWheelSegmentUseCase: DeleteWheelSegmentUseCase
) : ImmutableStateViewModel<ResultsUiState, ResultsUiEvent, ResultsUiEffect>() {

    private val route = savedStateHandle.toRoute<Destination.Results>()
    private val winnerWheelSegmentId = route.winnerWheelSegmentId

    override val uiState = getWheelSegmentStreamUseCase(winnerWheelSegmentId)
        .map { wheelSegment ->
            val wheelSegmentCount = getWheelSegmentsUseCase().count()
            val wheelSegmentUiState = toPresentation(wheelSegment)
            ResultsUiState(
                winnerUiState = wheelSegmentUiState,
                canDeleteWinner = wheelSegmentCount >= 3 // Cannot delete if only one item remains
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = ResultsUiState()
        )

    override fun onEvent(event: ResultsUiEvent) {
        when (event) {
            ResultsUiEvent.Spin -> triggerEffect(NavigateToSpin)
            ResultsUiEvent.SpinAndDelete -> {
                viewModelScope.launch {
                    deleteWheelSegmentUseCase(winnerWheelSegmentId)
                    triggerEffect(NavigateToSpin)
                }
            }
        }
    }
}