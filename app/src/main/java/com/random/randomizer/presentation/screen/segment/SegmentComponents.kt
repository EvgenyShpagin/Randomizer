package com.random.randomizer.presentation.screen.segment

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.random.randomizer.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditSegmentTopAppBar(
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