package com.random.randomizer.presentation.screen.results

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
    modifier: Modifier = Modifier,
    containerColor: Color = Color.Transparent
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = containerColor),
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
    if (showDeleteButton) {
        BoxWithConstraints(modifier = modifier) {
            if (this.maxWidth < 330.dp) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    DeleteAndSpinButton(
                        onClick = onDeleteAndSpinClicked,
                        modifier = Modifier.fillMaxWidth()
                    )
                    SpinButton(
                        onClick = onSpinClicked,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    DeleteAndSpinButton(
                        onClick = onDeleteAndSpinClicked,
                        modifier = Modifier.weight(1f)
                    )
                    SpinButton(
                        onClick = onSpinClicked,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    } else {
        SpinButton(
            onClick = onSpinClicked,
            modifier = modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun DeleteAndSpinButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(stringResource(R.string.button_delete_and_spin))
    }
}

@Composable
private fun SpinButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(stringResource(R.string.button_spin))
    }
}

@Preview("Both Spin Buttons Horizontal")
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

@Preview("Both Spin Buttons Vertical", widthDp = 329)
@Composable
private fun SpinButtonsVerticalPreview() {
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
fun GradientBackground(
    surfaceColor: Color = Color.Transparent,
    centerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    modifier: Modifier = Modifier
) {
    val gradientBrush = Brush.verticalGradient(
        0.25f to surfaceColor,
        0.5f to centerColor,
        0.75f to surfaceColor
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