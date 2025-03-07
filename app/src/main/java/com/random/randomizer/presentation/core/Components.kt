package com.random.randomizer.presentation.core

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.random.randomizer.R

@Composable
fun WheelSegment(
    itemUiState: WheelSegmentUiState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isClickable: Boolean = true
) {
    WheelSegment(
        title = itemUiState.title,
        description = itemUiState.description,
        containerColor = itemUiState.customColor,
        image = itemUiState.image,
        onClick = onClick,
        isClickable = isClickable,
        modifier = modifier
    )
}

@Composable
fun WheelSegment(
    title: String,
    description: String,
    image: ImageBitmap?,
    containerColor: Color?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isClickable: Boolean = true
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
            colors = ListItemDefaults.colors(
                containerColor = containerColor ?: MaterialTheme.colorScheme.surface
            ),
            modifier = if (isClickable) {
                Modifier
            } else {
                Modifier.clickable(enabled = false, onClick = {})
            }
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

object WheelSegmentDefaults {
    val CornerSize = 8.dp
}