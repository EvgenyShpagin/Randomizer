package com.random.randomizer.presentation.dialog.settings

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.AlertDialog
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
import com.random.randomizer.data.model.AndroidThemeOption as ThemeOption

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
        onCheckDarkThemeConfig = { viewModel.onEvent(SettingsUiEvent.SetDarkModeConfig(it)) },
        currentColorScheme = uiState.colorScheme,
        isColorSchemeEditable = uiState.isColorSchemeEditable,
        onCheckColorScheme = { viewModel.onEvent(SettingsUiEvent.SetColorScheme(it)) },
        currentDarkModeConfig = uiState.darkModeConfig
    )
}

@VisibleForTesting
@Composable
fun SettingsDialog(
    isLoading: Boolean,
    currentColorScheme: ThemeOption.ColorScheme,
    isColorSchemeEditable: Boolean,
    onCheckColorScheme: (ThemeOption.ColorScheme) -> Unit,
    currentDarkModeConfig: ThemeOption.DarkModeConfig,
    onCheckDarkThemeConfig: (ThemeOption.DarkModeConfig) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    onConfirm: () -> Unit = onDismiss
) {
    AlertDialog(
        title = {
            Text(stringResource(R.string.settings_title))
        },
        text = {
            StatefulContent(
                isLoading = isLoading,
                modifier = Modifier.wrapContentHeight(unbounded = true)
            ) {
                Column {
                    Text(
                        stringResource(R.string.label_color_scheme),
                        modifier = Modifier.paddingFromBaseline(bottom = 8.dp)
                    )
                    ColorSchemeButtons(
                        modifier = Modifier.fillMaxWidth(),
                        checkedColorScheme = currentColorScheme,
                        onCheckColorScheme = onCheckColorScheme,
                        isColorSchemeEditable = isColorSchemeEditable
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        stringResource(R.string.label_dark_mode_config),
                        modifier = Modifier.paddingFromBaseline(bottom = 8.dp)
                    )
                    DarkModeConfigButtons(
                        modifier = Modifier.fillMaxWidth(),
                        checkedDarkModeConfig = currentDarkModeConfig,
                        onCheckDarkThemeConfig = onCheckDarkThemeConfig
                    )
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

@Preview
@Composable
private fun SettingsDialogPreview() {
    SettingsDialog(
        isLoading = false,
        currentColorScheme = ThemeOption.ColorScheme.Static,
        isColorSchemeEditable = true,
        onCheckColorScheme = {},
        currentDarkModeConfig = ThemeOption.DarkModeConfig.System,
        onCheckDarkThemeConfig = {},
        onDismiss = {},
        onConfirm = {}
    )
}