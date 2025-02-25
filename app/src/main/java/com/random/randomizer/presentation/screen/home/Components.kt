package com.random.randomizer.presentation.screen.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import com.random.randomizer.R
import com.random.randomizer.presentation.core.WheelSegment
import com.random.randomizer.presentation.core.WheelSegmentDefaults
import com.random.randomizer.presentation.core.WheelSegmentUiState
import com.random.randomizer.presentation.theme.AppTheme
import kotlinx.coroutines.delay

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
fun AddSegmentSmallButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    SmallFloatingActionButton(
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.secondary,
        modifier = modifier
    ) {
        AddSegmentIcon()
    }
}

@Preview
@Composable
private fun AddSegmentSmallButtonPreview() {
    AppTheme {
        Surface {
            AddSegmentButton(onClick = {})
        }
    }
}

@Composable
fun AddSegmentButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.secondary,
        modifier = modifier
    ) {
        AddSegmentIcon()
    }
}

@Composable
private fun AddSegmentIcon() {
    Icon(
        Icons.Default.Add,
        stringResource(R.string.cd_add_new_wheel_segment)
    )
}

@Preview
@Composable
private fun AddSegmentButtonPreview() {
    AppTheme {
        Surface {
            AddSegmentSmallButton(onClick = {})
        }
    }
}

@Composable
fun DeletableWheelSegment(
    itemUiState: WheelSegmentUiState,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SwipeWheelSegmentContainer(onDelete = onDelete) {
        WheelSegment(
            itemUiState = itemUiState,
            onClick = onClick,
            modifier = modifier
        )
    }
}

@Composable
private fun SwipeWheelSegmentContainer(
    onDelete: () -> Unit,
    animationDuration: Int = 500,
    wheelSegment: @Composable () -> Unit
) {
    var isRemoved by remember { mutableStateOf(false) }

    val state = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                isRemoved = true
                true
            } else {
                false
            }
        }
    )

    LaunchedEffect(isRemoved) {
        if (isRemoved) {
            delay(animationDuration.toLong())
            onDelete()
        }
    }

    AnimatedVisibility(
        visible = !isRemoved,
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = animationDuration),
            shrinkTowards = Alignment.Top
        ) + fadeOut()
    ) {
        SwipeToDismissBox(
            state = state,
            backgroundContent = {
                DeleteWheelSegmentBackground(state = state)
            },
            content = {
                wheelSegment()
            },
            enableDismissFromStartToEnd = false
        )
    }
}

@Composable
private fun DeleteWheelSegmentBackground(state: SwipeToDismissBoxState) {
    val color = if (state.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
        MaterialTheme.colorScheme.errorContainer
    } else {
        Color.Transparent
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color, RoundedCornerShape(WheelSegmentDefaults.CornerSize))
            .padding(16.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onErrorContainer
        )
    }
}

@Composable
fun DeletableWheelSegmentList(
    wheelItems: List<WheelSegmentUiState>,
    onClick: (WheelSegmentUiState) -> Unit,
    onDelete: (WheelSegmentUiState) -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState()
) {
    val layoutDirection = LocalLayoutDirection.current
    val displayCutout = WindowInsets.displayCutout.asPaddingValues()
    val cutoutStartPadding = displayCutout.calculateStartPadding(layoutDirection)
    val cutoutEndPadding = displayCutout.calculateEndPadding(layoutDirection)

    LazyColumn(
        modifier = modifier,
        state = listState,
        contentPadding = PaddingValues(
            start = cutoutStartPadding.coerceAtLeast(16.dp),
            end = cutoutEndPadding.coerceAtLeast(16.dp)
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = wheelItems,
            key = { it.id }
        ) { item ->
            DeletableWheelSegment(
                itemUiState = item,
                onDelete = { onDelete(item) },
                onClick = { onClick(item) }
            )
        }
    }
}