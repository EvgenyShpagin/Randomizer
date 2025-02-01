package com.random.randomizer.presentation.screen.segment

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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