package com.random.randomizer.presentation.screen.home

import com.random.randomizer.presentation.core.BaseViewModel

class HomeViewModel : BaseViewModel<HomeUiState, HomeUiEvent, HomeUiEffect>(
    initialUiState = HomeUiState()
) {

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