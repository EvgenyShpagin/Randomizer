package com.random.randomizer.presentation.screen.home

import androidx.lifecycle.viewModelScope
import com.random.randomizer.domain.usecase.GetWheelSegmentsStreamUseCase
import com.random.randomizer.mapper.toPresentation
import com.random.randomizer.presentation.core.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getWheelSegmentsStreamUseCase: GetWheelSegmentsStreamUseCase,
) : BaseViewModel<HomeUiState, HomeUiEvent, HomeUiEffect>(
    initialUiState = HomeUiState()
) {

    init {
        getWheelSegmentsStreamUseCase()
            .onEach { segments ->
                val uiSegments = segments.map { it.toPresentation() }
                updateState { it.copy(wheelSegments = uiSegments) }
            }
            .launchIn(viewModelScope)
    }

    override fun onEvent(event: HomeUiEvent) {
        when (event) {
            HomeUiEvent.Edit -> onEdit()
            HomeUiEvent.Spin -> onSpin()
        }
    }

    private fun onSpin() {
        triggerEffect(HomeUiEffect.NavigateToSpin)
    }

    private fun onEdit() {
        triggerEffect(HomeUiEffect.NavigateToEdit)
    }
}