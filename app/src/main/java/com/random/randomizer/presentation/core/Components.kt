package com.random.randomizer.presentation.core

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import com.random.randomizer.R

@Composable
fun WheelSegment(
    itemUiState: WheelSegmentUiState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    WheelSegment(
        title = itemUiState.title,
        description = itemUiState.description,
        containerColor = itemUiState.customColor
            ?: MaterialTheme.colorScheme.surface,
        image = itemUiState.image,
        onClick = onClick,
        modifier = modifier
    )
}

@Composable
private fun WheelSegment(
    title: String,
    description: String,
    image: ImageBitmap?,
    containerColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        modifier = modifier,
        onClick = onClick
    ) {
        ListItem(
            headlineContent = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
            },
            supportingContent = if (description.isNotBlank()) {
                {
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                null
            },
            leadingContent = image?.let {
                {
                    Image(
                        bitmap = image,
                        contentDescription = null,
                        modifier = Modifier.size(56.dp)
                    )
                }
            },
            colors = ListItemDefaults.colors(containerColor = containerColor)
        )
    }
}

@Preview
@Composable
private fun WheelSegmentPreview() {
    WheelSegment(
        title = "My title",
        description = "My description",
        image = null,
        containerColor = MaterialTheme.colorScheme.surface,
        onClick = {}
    )
}

@Composable
fun WheelSegmentList(
    wheelItems: List<WheelSegmentUiState>,
    onClickWheelSegment: (WheelSegmentUiState) -> Unit,
    listState: LazyListState = rememberLazyListState(),
    modifier: Modifier = Modifier
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
            WheelSegment(
                itemUiState = item,
                onClick = { onClickWheelSegment(item) }
            )
        }
    }
}

@Composable
fun NoWheelSegmentsPlaceholder(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.label_no_wheel_segments),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}