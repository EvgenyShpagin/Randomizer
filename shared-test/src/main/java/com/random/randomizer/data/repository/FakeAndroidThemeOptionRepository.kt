package com.random.randomizer.data.repository

import com.random.randomizer.data.model.AndroidThemeOption
import com.random.randomizer.data.model.AndroidThemeOption.ColorScheme
import com.random.randomizer.data.model.AndroidThemeOption.DarkModeConfig
import com.random.randomizer.domain.model.ThemeOption
import com.random.randomizer.domain.repository.ThemeOptionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class FakeAndroidThemeOptionRepository @Inject constructor() : ThemeOptionRepository {

    var defaultColorScheme: ColorScheme = ColorScheme.Static
    var defaultDarkModeConfig: DarkModeConfig = DarkModeConfig.System

    private var optionsFlow = MutableStateFlow(
        value = listOf<AndroidThemeOption>(defaultColorScheme, defaultDarkModeConfig)
    )

    override suspend fun setThemeOption(themeOption: ThemeOption) {
        val currentOptions = optionsFlow.value
        val colorScheme = themeOption as? ColorScheme ?: currentOptions.first()
        val darkModeConfig = themeOption as? DarkModeConfig ?: currentOptions.last()
        optionsFlow.update { listOf(colorScheme, darkModeConfig) }
    }

    override fun getThemeOptionsStream(): Flow<List<ThemeOption>> {
        return optionsFlow
    }
}