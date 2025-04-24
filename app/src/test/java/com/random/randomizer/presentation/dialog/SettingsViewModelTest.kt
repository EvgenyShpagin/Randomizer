package com.random.randomizer.presentation.dialog

import com.random.randomizer.MainCoroutineRule
import com.random.randomizer.data.model.AndroidThemeOption
import com.random.randomizer.data.repository.FakeAndroidThemeOptionRepository
import com.random.randomizer.domain.usecase.GetThemeOptionsStreamUseCase
import com.random.randomizer.domain.usecase.SetThemeOptionUseCase
import com.random.randomizer.presentation.dialog.settings.SettingsUiEvent
import com.random.randomizer.presentation.dialog.settings.SettingsUiState
import com.random.randomizer.presentation.dialog.settings.SettingsViewModel
import com.random.randomizer.presentation.util.supportsDynamicTheming
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    // Set the main coroutines dispatcher for unit testing.
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    // Subject under test
    private lateinit var viewModel: SettingsViewModel

    private lateinit var repository: FakeAndroidThemeOptionRepository

    @Before
    fun setup() {
        repository = FakeAndroidThemeOptionRepository()
        viewModel = SettingsViewModel(
            GetThemeOptionsStreamUseCase(repository),
            SetThemeOptionUseCase(repository)
        )
    }

    @Test
    fun updatesState_whenCreated() = runTest {
        // Given - Dynamic color scheme and set Dark mode
        val colorScheme = AndroidThemeOption.ColorScheme.Dynamic
        val darkModeConfig = AndroidThemeOption.DarkModeConfig.Dark
        repository.setThemeOption(colorScheme)
        repository.setThemeOption(darkModeConfig)

        // When - on startup
        val actualState = viewModel.uiState.first()

        // Then - verify state is correct
        val expectedState = SettingsUiState(
            isLoading = false,
            darkModeConfig = darkModeConfig,
            colorScheme = colorScheme,
            isColorSchemeEditable = supportsDynamicTheming()
        )
        assertEquals(expectedState, actualState)
    }

    @Test
    fun setsColorScheme() = runTest {
        // Given - static color scheme
        repository.setThemeOption(AndroidThemeOption.ColorScheme.Static)

        // When - on SetColorScheme event
        viewModel.onEvent(
            SettingsUiEvent.SetColorScheme(AndroidThemeOption.ColorScheme.Dynamic)
        )

        // Then - verify color scheme changed
        val themeOptions = repository.getThemeOptionsStream().first()
        val actualColorScheme = themeOptions.find { it is AndroidThemeOption.ColorScheme }
        val expectedColorScheme = AndroidThemeOption.ColorScheme.Dynamic
        assertEquals(expectedColorScheme, actualColorScheme)
    }

    @Test
    fun setsDarkModeConfig() = runTest {
        // Given - follow system dark mode config
        repository.setThemeOption(AndroidThemeOption.DarkModeConfig.System)

        // When - on SetDarkModeConfig event
        viewModel.onEvent(
            SettingsUiEvent.SetDarkModeConfig(AndroidThemeOption.DarkModeConfig.Dark)
        )

        // Then - verify dark mode config changed
        val themeOptions = repository.getThemeOptionsStream().first()
        val actualDarkModeConfig = themeOptions.find { it is AndroidThemeOption.DarkModeConfig }
        val expectedDarkModeConfig = AndroidThemeOption.DarkModeConfig.Dark
        assertEquals(expectedDarkModeConfig, actualDarkModeConfig)
    }
}