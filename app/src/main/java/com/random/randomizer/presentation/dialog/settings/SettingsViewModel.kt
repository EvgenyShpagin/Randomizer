package com.random.randomizer.presentation.dialog.settings

import androidx.lifecycle.viewModelScope
import com.random.randomizer.data.model.AndroidThemeOption
import com.random.randomizer.domain.usecase.GetThemeOptionsStreamUseCase
import com.random.randomizer.domain.usecase.SetThemeOptionUseCase
import com.random.randomizer.presentation.core.ImmutableStateViewModel
import com.random.randomizer.presentation.util.supportsDynamicTheming
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    getThemeOptionsStreamUseCase: GetThemeOptionsStreamUseCase,
    private val setThemeOptionUseCase: SetThemeOptionUseCase
) : ImmutableStateViewModel<SettingsUiState, SettingsUiEvent, Nothing>() {

    override val uiState: StateFlow<SettingsUiState> = getThemeOptionsStreamUseCase()
        .map { themeOptions ->
            val colorScheme = themeOptions.find { it is AndroidThemeOption.ColorScheme }
            val darkModeConfig = themeOptions.find { it is AndroidThemeOption.DarkModeConfig }
            SettingsUiState(
                isLoading = false,
                isColorSchemeEditable = supportsDynamicTheming(),
                colorScheme = colorScheme as AndroidThemeOption.ColorScheme,
                darkModeConfig = darkModeConfig as AndroidThemeOption.DarkModeConfig
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SettingsUiState()
        )

    override fun onEvent(event: SettingsUiEvent) {
        viewModelScope.launch {
            when (event) {
                is SettingsUiEvent.SetColorScheme -> setThemeOptionUseCase(event.colorScheme)
                is SettingsUiEvent.SetDarkModeConfig -> setThemeOptionUseCase(event.darkModeConfig)
            }
        }
    }
}