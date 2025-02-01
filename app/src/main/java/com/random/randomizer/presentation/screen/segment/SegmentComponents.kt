package com.random.randomizer.presentation.screen.segment

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.random.randomizer.R
import com.random.randomizer.presentation.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WheelSegmentTopAppBar(
    onNavigationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(stringResource(R.string.edit_segment_screen))
        },
        navigationIcon = {
            IconButton(onClick = onNavigationClick) {
                Icon(Icons.Default.Close, contentDescription = null)
            }
        },
        modifier = modifier
    )
}

@Preview
@Composable
private fun WheelSegmentTopAppBarPreview() {
    AppTheme {
        WheelSegmentTopAppBar(onNavigationClick = {})
    }
}

@Composable
fun SectionHeader(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        modifier = modifier
    )
}

@Preview
@Composable
private fun SectionHeaderPreview() {
    SectionHeader(stringResource(R.string.label_preview))
}

@Composable
fun SaveButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        contentPadding = ButtonDefaults.ButtonWithIconContentPadding
    ) {
        Icon(
            Icons.Default.Check,
            contentDescription = stringResource(R.string.button_save),
            modifier = Modifier.size(ButtonDefaults.IconSize)
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(stringResource(R.string.button_save))
    }
}

@Preview
@Composable
private fun SaveButtonPreview() {
    SaveButton(onClick = {})
}