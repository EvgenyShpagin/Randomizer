package com.random.randomizer.presentation.dialog.settings

import com.random.randomizer.presentation.core.UiState
import com.random.randomizer.data.model.AndroidThemeOption as ThemeOption

data class SettingsUiState(
    val isLoading: Boolean = true,
    val colorScheme: ThemeOption.ColorScheme = ThemeOption.ColorScheme.Static,
    val isColorSchemeEditable: Boolean = true,
    val darkModeConfig: ThemeOption.DarkModeConfig = ThemeOption.DarkModeConfig.System
) : UiState