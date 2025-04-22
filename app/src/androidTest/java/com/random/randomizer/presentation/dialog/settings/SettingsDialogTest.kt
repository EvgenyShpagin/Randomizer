package com.random.randomizer.presentation.dialog.settings

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.hilt.navigation.compose.hiltViewModel
import com.random.randomizer.HiltTestActivity
import com.random.randomizer.R
import com.random.randomizer.data.model.AndroidThemeOption
import com.random.randomizer.data.repository.FakeAndroidThemeOptionRepository
import com.random.randomizer.domain.repository.ThemeOptionRepository
import com.random.randomizer.presentation.util.supportsDynamicTheming
import com.random.randomizer.test_util.stringResource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class SettingsDialogTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    @Inject
    lateinit var repository: ThemeOptionRepository

    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setup() {
        hiltRule.inject()
        (repository as FakeAndroidThemeOptionRepository).apply {
            defaultColorScheme = AndroidThemeOption.ColorScheme.Static
            defaultDarkModeConfig = AndroidThemeOption.DarkModeConfig.System
        }
    }

    @Test
    fun showsDisabledButtons_whenNoDynamicColorSupport() {
        mockDynamicThemingSupport(support = false)

        setContent()

        composeTestRule
            .onNodeWithText(stringResource(R.string.color_scheme_dynamic))
            .assertIsNotEnabled()
    }

    @Test
    fun showsEnabledButtons_whenHasDynamicColorSupport() {
        mockDynamicThemingSupport(support = true)

        setContent()

        composeTestRule
            .onNodeWithText(stringResource(R.string.color_scheme_dynamic))
            .assertIsEnabled()
    }

    @Test
    fun setsColorScheme_onClickWhenHasDynamicColorSupport() {
        mockDynamicThemingSupport(support = true)

        setContent()

        composeTestRule
            .onNodeWithText(stringResource(R.string.color_scheme_static))
            .assertIsSelected()

        composeTestRule
            .onNodeWithText(stringResource(R.string.color_scheme_dynamic))
            .performClick()
            .assertIsSelected()
    }

    @Test
    fun setsDarkModeConfig_onClick() {
        mockDynamicThemingSupport(support = true)

        setContent()

        composeTestRule
            .onNodeWithText(stringResource(R.string.dark_mode_config_system))
            .assertIsSelected()

        composeTestRule
            .onNodeWithText(stringResource(R.string.dark_mode_config_light))
            .performClick()
            .assertIsSelected()
    }

    private fun mockDynamicThemingSupport(support: Boolean) {
        mockkStatic("com.random.randomizer.presentation.util.BuildUtilsKt")
        every { supportsDynamicTheming() } returns support
    }

    private fun setContent() {
        composeTestRule.setContent {
            viewModel = hiltViewModel()
            SettingsDialog(
                viewModel = viewModel,
                onConfirm = {},
                onDismiss = {},
            )
        }
    }
}