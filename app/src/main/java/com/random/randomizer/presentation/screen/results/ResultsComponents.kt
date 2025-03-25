package com.random.randomizer.presentation.screen.results

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.random.randomizer.R
import com.random.randomizer.presentation.theme.RandomizerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsTopAppBar(
    onNavigationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(R.string.results_screen_title)) },
        navigationIcon = {
            IconButton(onClick = onNavigationClick) {
                Icon(Icons.AutoMirrored.Default.ArrowBack, null)
            }
        },
        modifier = modifier
    )
}

@Preview
@Composable
private fun NavigationButtonPreview() {
    RandomizerTheme {
        Surface {
            ResultsTopAppBar(onNavigationClick = {})
        }
    }
}

@Composable
fun SpinButtons(
    onSpinClicked: () -> Unit,
    onDeleteAndSpinClicked: () -> Unit,
    showDeleteButton: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        if (showDeleteButton) {
            OutlinedButton(
                onClick = onDeleteAndSpinClicked,
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(R.string.button_delete_and_spin))
            }
        }

        Button(
            onClick = onSpinClicked,
            modifier = Modifier.weight(1f)
        ) {
            Text(stringResource(R.string.button_spin))
        }
    }
}

@Preview("Both Spin Buttons")
@Composable
private fun SpinButtonsPreview() {
    RandomizerTheme {
        Surface {
            SpinButtons(
                onSpinClicked = {},
                onDeleteAndSpinClicked = {},
                showDeleteButton = true
            )
        }
    }
}

@Preview("Without Delete Button")
@Composable
private fun SpinButtonWithoutDeletePreview() {
    RandomizerTheme {
        Surface {
            SpinButtons(
                onSpinClicked = {},
                onDeleteAndSpinClicked = {},
                showDeleteButton = false
            )
        }
    }
}

@Composable
fun GradientBackground(modifier: Modifier = Modifier) {
    val gradientBrush = Brush.verticalGradient(
        0.25f to MaterialTheme.colorScheme.background,
        0.5f to MaterialTheme.colorScheme.primaryContainer,
        0.75f to MaterialTheme.colorScheme.background
    )
    Canvas(modifier = modifier.fillMaxWidth()) {
        drawRect(brush = gradientBrush)
    }
}

@PreviewLightDark
@Composable
fun GradientBackgroundPreview() {
    RandomizerTheme {
        Surface {
            GradientBackground(modifier = Modifier.size(200.dp, 400.dp))
        }
    }
}