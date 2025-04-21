package com.random.randomizer.presentation.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.random.randomizer.R
import com.random.randomizer.presentation.core.StatefulContent
import com.random.randomizer.presentation.core.ThemeOptions

@Composable
fun SettingsDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit = onDismiss,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SettingsDialog(
        onDismiss = onDismiss,
        onConfirm = onConfirm,
        isLoading = uiState.isLoading,
        onClickDarkThemeConfig = { viewModel.onEvent(SettingsUiEvent.SetDarkModeConfig(it)) },
        colorScheme = uiState.colorScheme,
        isColorSchemeEditable = uiState.isColorSchemeEditable,
        onClickColorScheme = { viewModel.onEvent(SettingsUiEvent.SetColorScheme(it)) },
        darkModeConfig = uiState.darkModeConfig,
    )
}

@Composable
private fun SettingsDialog(
    isLoading: Boolean,
    colorScheme: ThemeOptions.ColorScheme,
    isColorSchemeEditable: Boolean,
    onClickColorScheme: (ThemeOptions.ColorScheme) -> Unit,
    darkModeConfig: ThemeOptions.DarkModeConfig,
    onClickDarkThemeConfig: (ThemeOptions.DarkModeConfig) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    onConfirm: () -> Unit = onDismiss
) {
    AlertDialog(
        title = {
            Text(stringResource(R.string.settings_title))
        },
        text = {
            StatefulContent(isLoading = isLoading, modifier = Modifier.fillMaxSize()) {
                Column {
                    Text(
                        stringResource(R.string.label_color_scheme),
                        modifier = Modifier.paddingFromBaseline(bottom = 8.dp)
                    )
                    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                        val allColorSchemes = ThemeOptions.ColorScheme.entries
                        allColorSchemes.forEachIndexed { index, colorSchemeEntry ->
                            SegmentedButton(
                                selected = colorSchemeEntry == colorScheme,
                                onClick = { onClickColorScheme(colorSchemeEntry) },
                                shape = SegmentedButtonDefaults.itemShape(
                                    index, allColorSchemes.count()
                                ),
                                enabled = isColorSchemeEditable
                            ) {
                                Text(colorSchemeEntry.stringResource())
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    Text(
                        stringResource(R.string.label_dark_mode_config),
                        modifier = Modifier.paddingFromBaseline(bottom = 8.dp)
                    )
                    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                        val allDarkModeConfigs = ThemeOptions.DarkModeConfig.entries
                        allDarkModeConfigs.forEachIndexed { index, darkThemeConfigEntry ->
                            SegmentedButton(
                                selected = darkThemeConfigEntry == darkModeConfig,
                                onClick = { onClickDarkThemeConfig(darkThemeConfigEntry) },
                                shape = SegmentedButtonDefaults.itemShape(
                                    index, allDarkModeConfigs.count()
                                )
                            ) {
                                Text(darkThemeConfigEntry.stringResource())
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(android.R.string.ok))
            }
        },
        onDismissRequest = onDismiss,
        modifier = modifier
    )
}

@Composable
private fun ThemeOptions.ColorScheme.stringResource(): String {
    return when (this) {
        ThemeOptions.ColorScheme.Static -> stringResource(R.string.color_scheme_static)
        ThemeOptions.ColorScheme.Dynamic -> stringResource(R.string.color_scheme_dynamic)
    }
}

@Composable
private fun ThemeOptions.DarkModeConfig.stringResource(): String {
    return when (this) {
        ThemeOptions.DarkModeConfig.Light -> stringResource(R.string.dark_mode_config_light)
        ThemeOptions.DarkModeConfig.System -> stringResource(R.string.dark_mode_config_system)
        ThemeOptions.DarkModeConfig.Dark -> stringResource(R.string.dark_mode_config_dark)
    }
}

@Preview
@Composable
private fun SettingsDialogPreview() {
    SettingsDialog(
        isLoading = false,
        colorScheme = ThemeOptions.ColorScheme.Static,
        isColorSchemeEditable = true,
        onClickColorScheme = {},
        darkModeConfig = ThemeOptions.DarkModeConfig.System,
        onClickDarkThemeConfig = {},
        onDismiss = {},
        onConfirm = {}
    )
}