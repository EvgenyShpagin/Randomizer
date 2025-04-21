package com.random.randomizer.presentation.dialog

import com.random.randomizer.presentation.core.ThemeOptions
import com.random.randomizer.presentation.core.UiState

data class SettingsUiState(
    val isLoading: Boolean = true,
    val colorScheme: ThemeOptions.ColorScheme = ThemeOptions.ColorScheme.Static,
    val isColorSchemeEditable: Boolean = true,
    val darkModeConfig: ThemeOptions.DarkModeConfig = ThemeOptions.DarkModeConfig.System
) : UiState