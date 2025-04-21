package com.random.randomizer.presentation.dialog

import com.random.randomizer.presentation.core.ThemeOptions
import com.random.randomizer.presentation.core.UiEvent

sealed interface SettingsUiEvent : UiEvent {
    data class SetColorScheme(val colorScheme: ThemeOptions.ColorScheme) : SettingsUiEvent
    data class SetDarkModeConfig(val darkModeConfig: ThemeOptions.DarkModeConfig) : SettingsUiEvent
}