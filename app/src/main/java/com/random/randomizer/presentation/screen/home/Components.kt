package com.random.randomizer.presentation.screen.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.random.randomizer.R

@Composable
fun SpinButton(
    onClick: () -> Unit,
    show: Boolean,
    modifier: Modifier = Modifier
) {
    if (show) {
        ExtendedFloatingActionButton(
            icon = {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null
                )
            },
            text = {
                Text(stringResource(R.string.button_spin))
            },
            onClick = onClick,
            modifier = modifier
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    onClickEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(text = stringResource(R.string.home_screen_title))
        },
        actions = {
            IconButton(onClick = onClickEdit) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.cd_edit_wheel_segments),
                    modifier = Modifier.padding(end = 16.dp)
                )
            }
        }
    )
}