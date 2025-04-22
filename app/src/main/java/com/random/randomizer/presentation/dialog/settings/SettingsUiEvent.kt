package com.random.randomizer.presentation.dialog.settings

import com.random.randomizer.data.model.AndroidThemeOption as ThemeOption
import com.random.randomizer.presentation.core.UiEvent

sealed interface SettingsUiEvent : UiEvent {
    data class SetColorScheme(val colorScheme: ThemeOption.ColorScheme) : SettingsUiEvent
    data class SetDarkModeConfig(val darkModeConfig: ThemeOption.DarkModeConfig) : SettingsUiEvent
}