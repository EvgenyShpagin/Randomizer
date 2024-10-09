package com.random.randomizer.presentation.screen.edit

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
fun EditTopAppBar(
    onNavigationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(stringResource(R.string.edit_screen_title))
        },
        navigationIcon = {
            IconButton(onClick = onNavigationClick) {
                Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = null)
            }
        },
        modifier = modifier
    )
}

@Composable
fun AddWheelSegmentButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(R.string.cd_add_new_wheel_segment)
        )
    }
}