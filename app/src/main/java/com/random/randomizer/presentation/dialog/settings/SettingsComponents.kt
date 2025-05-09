package com.random.randomizer.presentation.dialog.settings

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonColors
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.random.randomizer.R
import com.random.randomizer.presentation.theme.RandomizerTheme
import com.random.randomizer.data.model.AndroidThemeOption as ThemeOption

@Composable
fun ColorSchemeButtons(
    modifier: Modifier = Modifier,
    checkedColorScheme: ThemeOption.ColorScheme,
    onCheckColorScheme: (ThemeOption.ColorScheme) -> Unit,
    isColorSchemeEditable: Boolean
) {
    SingleChoiceSegmentedButtonRow(modifier = modifier) {
        val allColorSchemes = ThemeOption.ColorScheme.entries
        val colors = buttonColors()
        allColorSchemes.forEachIndexed { index, colorScheme ->
            SegmentedButton(
                colors = colors,
                selected = colorScheme == checkedColorScheme,
                onClick = { onCheckColorScheme(colorScheme) },
                shape = SegmentedButtonDefaults.itemShape(index, allColorSchemes.count()),
                enabled = isColorSchemeEditable
            ) {
                Text(colorScheme.stringResource())
            }
        }
    }
}

@Preview
@Composable
private fun ColorSchemeButtonsEnabledPreview() {
    RandomizerTheme {
        ColorSchemeButtons(
            checkedColorScheme = ThemeOption.ColorScheme.Static,
            onCheckColorScheme = {},
            isColorSchemeEditable = true
        )
    }
}

@Preview
@Composable
private fun ColorSchemeButtonsDisabledPreview() {
    RandomizerTheme {
        ColorSchemeButtons(
            checkedColorScheme = ThemeOption.ColorScheme.Static,
            onCheckColorScheme = {},
            isColorSchemeEditable = false
        )
    }
}

@Composable
fun DarkModeConfigButtons(
    modifier: Modifier = Modifier,
    checkedDarkModeConfig: ThemeOption.DarkModeConfig,
    onCheckDarkThemeConfig: (ThemeOption.DarkModeConfig) -> Unit,
) {
    SingleChoiceSegmentedButtonRow(modifier = modifier) {
        val allDarkModeConfigs = ThemeOption.DarkModeConfig.entries
        val colors = buttonColors()
        allDarkModeConfigs.forEachIndexed { index, darkThemeConfig ->
            SegmentedButton(
                colors = colors,
                selected = darkThemeConfig == checkedDarkModeConfig,
                onClick = { onCheckDarkThemeConfig(darkThemeConfig) },
                shape = SegmentedButtonDefaults.itemShape(index, allDarkModeConfigs.count())
            ) {
                Text(
                    text = darkThemeConfig.stringResource(),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
        }
    }
}

@Preview
@Composable
private fun DarkModeConfigButtonsPreview() {
    RandomizerTheme {
        DarkModeConfigButtons(
            checkedDarkModeConfig = ThemeOption.DarkModeConfig.System,
            onCheckDarkThemeConfig = {}
        )
    }
}

@Composable
private fun ThemeOption.stringResource(): String {
    return when (this) {
        ThemeOption.ColorScheme.Static -> stringResource(R.string.color_scheme_static)
        ThemeOption.ColorScheme.Dynamic -> stringResource(R.string.color_scheme_dynamic)
        ThemeOption.DarkModeConfig.Light -> stringResource(R.string.dark_mode_config_light)
        ThemeOption.DarkModeConfig.System -> stringResource(R.string.dark_mode_config_system)
        ThemeOption.DarkModeConfig.Dark -> stringResource(R.string.dark_mode_config_dark)
    }
}

@Composable
private fun buttonColors(): SegmentedButtonColors {
    return SegmentedButtonDefaults.colors(
        inactiveContainerColor = Color.Transparent,
        disabledInactiveContainerColor = Color.Transparent,
        disabledInactiveBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
        disabledInactiveContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
    )
}