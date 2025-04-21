package com.random.randomizer.presentation.dialog

import com.random.randomizer.presentation.core.ImmutableStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ImmutableStateViewModel<SettingsUiState, SettingsUiEvent, Nothing>() {
    override val uiState: StateFlow<SettingsUiState> = TODO("Not yet implemented")

    override fun onEvent(event: SettingsUiEvent) {
        TODO("Not yet implemented")
    }
}