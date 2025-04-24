package com.random.randomizer.presentation.core

import com.random.randomizer.data.model.AndroidThemeOption.ColorScheme
import com.random.randomizer.data.model.AndroidThemeOption.DarkModeConfig

data class MainActivityUiState(
    val isLoading: Boolean = true,
    val darkModeConfig: DarkModeConfig = DarkModeConfig.System,
    val colorScheme: ColorScheme = ColorScheme.Static
) : UiState