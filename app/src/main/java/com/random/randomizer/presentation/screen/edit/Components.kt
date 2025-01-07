package com.random.randomizer.presentation.screen.edit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.random.randomizer.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTopAppBar(
    onNavigationClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
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
        scrollBehavior = scrollBehavior,
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

@Composable
fun SegmentTitleTextField(
    title: String,
    onInput: (String) -> Unit,
    maxLines: Int = 1,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = title,
        onValueChange = onInput,
        maxLines = maxLines,
        label = {
            Text(stringResource(R.string.label_segment_title))
        },
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun SegmentDescriptionTextField(
    description: String,
    onInput: (String) -> Unit,
    maxLines: Int = 1,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = description,
        onValueChange = onInput,
        maxLines = maxLines,
        label = {
            Text(stringResource(R.string.label_segment_description))
        },
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun AddSegmentImageButton(
    onClickAdd: () -> Unit,
    modifier: Modifier = Modifier
) {
    val contentDescription = stringResource(R.string.cd_add_image_to_segment)
    FilledTonalButton(
        onClick = onClickAdd,
        modifier = modifier
            .clearAndSetSemantics { this.contentDescription = contentDescription }
            .fillMaxWidth()
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            modifier = Modifier.size(ButtonDefaults.IconSize)
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(stringResource(R.string.button_add_segment_image))
    }
}

@Composable
fun RemoveSegmentImageButton(
    onClickRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    val contentDescription = stringResource(R.string.cd_remove_image_from_segment)
    FilledTonalButton(
        onClick = onClickRemove,
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer
        ),
        modifier = modifier
            .clearAndSetSemantics { this.contentDescription = contentDescription }
            .fillMaxWidth()
    ) {
        Icon(
            imageVector = Icons.Outlined.Delete,
            contentDescription = null,
            modifier = Modifier.size(ButtonDefaults.IconSize)
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(stringResource(R.string.button_remove_segment_image))
    }
}

@Composable
fun SegmentColorsRow(
    colors: List<Color>,
    onCheckColor: (Color) -> Unit,
    onRemoveColor: () -> Unit,
    checkedColor: Color? = null,
    modifier: Modifier = Modifier
) {
    val itemModifier = Modifier
        .padding(4.dp)
        .size(40.dp)

    Row(modifier = modifier) {
        AnimatedVisibility(
            visible = checkedColor != null,
            enter = fadeIn() + expandHorizontally(),
            exit = fadeOut() + shrinkHorizontally()
        ) {
            Row {
                RemoveSegmentColorIconButton(onCheck = onRemoveColor, modifier = itemModifier)
                Spacer(Modifier.padding(end = 4.dp))
            }
        }
        LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            if (checkedColor != null && checkedColor !in colors) {
                item {
                    SegmentColorCircle(
                        color = checkedColor,
                        isChecked = true,
                        onCheck = { onCheckColor(checkedColor) },
                        modifier = itemModifier
                    )
                }
            }
            items(items = colors, key = { it.hashCode() }) { color ->
                SegmentColorCircle(
                    color = color,
                    isChecked = color == checkedColor,
                    onCheck = { onCheckColor(color) },
                    modifier = itemModifier
                )
            }
        }
    }
}

@Composable
fun SegmentColorCircle(
    color: Color,
    isChecked: Boolean,
    onCheck: () -> Unit,
    modifier: Modifier = Modifier
) {
    val contentDescriptionResId = if (isChecked) {
        R.string.cd_checked_color_circle
    } else {
        R.string.cd_unchecked_color_circle
    }
    val contentDescription = stringResource(contentDescriptionResId, color.toString())

    Canvas(
        modifier = modifier
            .clickable(
                onClick = onCheck,
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = false),
                role = Role.Button,
                onClickLabel = stringResource(R.string.action_set_background_color)
            )
            .semantics { this.contentDescription = contentDescription }
    ) {
        if (isChecked) {
            drawCircle(color = color, style = Stroke(2.dp.toPx()))
            inset(4.dp.toPx()) {
                drawCircle(color = color, style = Fill)
            }
        } else {
            drawCircle(color = color, style = Fill)
        }
    }
}

@Composable
fun RemoveSegmentColorIconButton(
    onCheck: () -> Unit,
    modifier: Modifier = Modifier
) {
    val contentDescription = stringResource(R.string.cd_remove_color)

    FilledIconButton(
        onClick = onCheck,
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer
        ),
        shape = ShapeDefaults.Small,
        modifier = modifier
            .clearAndSetSemantics { this.contentDescription = contentDescription }
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null
        )
    }
}

@Preview(heightDp = 48)
@Composable
private fun SegmentColorsRowPreview() {
    SegmentColorsRow(
        colors = listOf(Color.Blue, Color.Yellow),
        checkedColor = null,
        onRemoveColor = {},
        onCheckColor = {}
    )
}