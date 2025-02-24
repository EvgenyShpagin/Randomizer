package com.random.randomizer.presentation.screen.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.random.randomizer.R
import com.random.randomizer.presentation.theme.AppTheme

@Composable
fun SpinButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
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

@Preview
@Composable
private fun SpinButtonPreview() {
    AppTheme {
        Surface {
            SpinButton({})
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = {
            Text(text = stringResource(R.string.home_screen_title))
        },
        scrollBehavior = scrollBehavior,
        modifier = modifier
    )
}

@Preview
@Composable
private fun HomeTopBarPreview() {
    @OptIn(ExperimentalMaterial3Api::class)
    AppTheme {
        Surface {
            HomeTopBar(scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior())
        }
    }
}

@Composable
fun AddSegmentButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    SmallFloatingActionButton(
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.secondary,
        modifier = modifier
    ) {
        Icon(
            Icons.Default.Add,
            stringResource(R.string.cd_add_new_wheel_segment)
        )
    }
}

@Preview
@Composable
private fun AddSegmentButtonPreview() {
    AppTheme {
        Surface {
            AddSegmentButton(onClick = {})
        }
    }
}