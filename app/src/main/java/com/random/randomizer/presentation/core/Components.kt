package com.random.randomizer.presentation.core

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun WheelSegment(
    itemUiState: WheelSegmentUiState,
    modifier: Modifier = Modifier
) {
    WheelSegment(
        title = itemUiState.title,
        description = itemUiState.description,
        containerColor = itemUiState.customColor
            ?: MaterialTheme.colorScheme.surface,
        image = itemUiState.image,
        onClick = itemUiState.onClick,
        modifier = modifier
    )
}

@Composable
private fun WheelSegment(
    title: String,
    description: String?,
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
            supportingContent = description?.let {
                {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
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

@Composable
fun WheelSegmentList(
    wheelItems: List<WheelSegmentUiState>,
    listState: LazyListState = rememberLazyListState(),
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        this.items(
            items = wheelItems,
            key = { it.id }
        ) { item ->
            WheelSegment(itemUiState = item)
        }
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