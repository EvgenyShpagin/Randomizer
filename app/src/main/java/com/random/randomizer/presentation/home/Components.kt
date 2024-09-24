package com.random.randomizer.presentation.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.random.randomizer.R

@Composable
fun SpinButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ExtendedFloatingActionButton(
        icon = {
            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
        },
        text = {
            Text(stringResource(R.string.button_spin))
        },
        onClick = onClick,
        modifier = modifier
    )
}