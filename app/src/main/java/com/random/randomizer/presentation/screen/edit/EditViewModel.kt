package com.random.randomizer.presentation.screen.edit

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class EditViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(EditUiState())
    val uiState = _uiState.asStateFlow()
}