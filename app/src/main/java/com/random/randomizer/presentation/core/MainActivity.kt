package com.random.randomizer.presentation.core

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.random.randomizer.data.model.AndroidThemeOption.ColorScheme
import com.random.randomizer.data.model.AndroidThemeOption.DarkModeConfig
import com.random.randomizer.presentation.theme.RandomizerTheme
import com.random.randomizer.presentation.util.isSystemInDarkThemeFlow
import com.random.randomizer.presentation.util.supportsTransparentNavigationBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

private val LightScrim = android.graphics.Color.argb(0xe6, 0xFF, 0xFF, 0xFF)
private val DarkScrim = android.graphics.Color.argb(0x80, 0x1b, 0x1b, 0x1b)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        // We keep this as a mutable state, so that we can track changes inside the composition.
        // This allows us to react to dark/light mode changes.
        var themeOptions by mutableStateOf(ThemeOptions())

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                combine(
                    viewModel.uiState,
                    isSystemInDarkThemeFlow
                ) { uiState, isDarkTheme ->
                    ThemeOptions(
                        isDarkTheme = when (uiState.darkModeConfig) {
                            DarkModeConfig.Light -> false
                            DarkModeConfig.System -> isDarkTheme
                            DarkModeConfig.Dark -> true
                        },
                        dynamicColorsEnabled = uiState.colorScheme == ColorScheme.Dynamic
                    )
                }
                    .onEach { themeOptions = it }
                    .map { it.isDarkTheme }
                    .distinctUntilChanged()
                    .collect { shouldBeInDarkTheme ->
                        enableEdgeToEdge(
                            statusBarStyle = SystemBarStyle.auto(
                                lightScrim = android.graphics.Color.TRANSPARENT,
                                darkScrim = android.graphics.Color.TRANSPARENT,
                            ) { shouldBeInDarkTheme },
                            navigationBarStyle = SystemBarStyle.auto(
                                lightScrim = LightScrim,
                                darkScrim = DarkScrim,
                            ) { shouldBeInDarkTheme },
                        )

                        if (supportsTransparentNavigationBar()) {
                            window.setNavigationBarContrastEnforced(false)
                        }
                    }
            }
        }

        splashScreen.setKeepOnScreenCondition { viewModel.uiState.value.isLoading }

        setContent {
            RandomizerTheme(
                darkTheme = themeOptions.isDarkTheme,
                dynamicColor = themeOptions.dynamicColorsEnabled
            ) {
                RandomizerApp()
            }
        }
    }

    /**
     * This wrapping class allows us to combine all the changes and prevent unnecessary recompositions.
     */
    private data class ThemeOptions(
        val isDarkTheme: Boolean = false,
        val dynamicColorsEnabled: Boolean = false
    )
}