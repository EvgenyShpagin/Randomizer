package com.random.randomizer.data.model

import com.random.randomizer.domain.model.ThemeOption

sealed interface AndroidThemeOption : ThemeOption {
    enum class DarkModeConfig : AndroidThemeOption { Light, System, Dark }
    enum class ColorScheme : AndroidThemeOption { Static, Dynamic }
}