package com.random.randomizer.presentation.dialog.settings

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.random.randomizer.data.model.AndroidThemeOption

@Preview
@Composable
private fun LightTheme_Loading_Preview() {
    SettingsDialog(isLoading = true)
}

@Preview
@Composable
private fun LightTheme_EditableColorScheme_Preview() {
    SettingsDialog(isColorSchemeEditable = true)
}

@Preview
@Composable
private fun LightTheme_DisabledColorScheme_Preview() {
    SettingsDialog(isColorSchemeEditable = false)
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
private fun DarkTheme_EditableColorScheme_Preview() {
    SettingsDialog(isColorSchemeEditable = true)
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    fontScale = 1.5f
)
@Composable
private fun DarkTheme_DisabledColorScheme_Scale150_Preview() {
    SettingsDialog(isColorSchemeEditable = true)
}


@Composable
private fun SettingsDialog(
    isLoading: Boolean = false,
    currentDarkModeConfig: AndroidThemeOption.DarkModeConfig = AndroidThemeOption.DarkModeConfig.System,
    currentColorScheme: AndroidThemeOption.ColorScheme = AndroidThemeOption.ColorScheme.Static,
    isColorSchemeEditable: Boolean = true,
) {
    SettingsDialog(
        isLoading = isLoading,
        currentColorScheme = currentColorScheme,
        isColorSchemeEditable = isColorSchemeEditable,
        onCheckColorScheme = {},
        currentDarkModeConfig = currentDarkModeConfig,
        onCheckDarkThemeConfig = {},
        onDismiss = {},
        onConfirm = {},
        modifier = Modifier
    )
}