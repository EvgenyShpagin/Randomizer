package com.random.randomizer.presentation.core

import androidx.lifecycle.viewModelScope
import com.random.randomizer.data.model.AndroidThemeOption.ColorScheme
import com.random.randomizer.data.model.AndroidThemeOption.DarkModeConfig
import com.random.randomizer.domain.usecase.GetThemeOptionsStreamUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    getThemeOptionsStreamUseCase: GetThemeOptionsStreamUseCase
) : ImmutableStateViewModel<MainActivityUiState, Nothing, Nothing>() {

    override val uiState = getThemeOptionsStreamUseCase()
        .map { themeOptions ->
            val darkModeConfig = themeOptions.find { it is DarkModeConfig }
            val colorScheme = themeOptions.find { it is ColorScheme }
            MainActivityUiState(
                isLoading = false,
                darkModeConfig = darkModeConfig as DarkModeConfig,
                colorScheme = colorScheme as ColorScheme
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MainActivityUiState()
        )

    override fun onEvent(event: Nothing) {}
}