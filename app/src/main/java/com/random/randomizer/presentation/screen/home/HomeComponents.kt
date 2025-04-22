@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.random.randomizer.presentation.screen.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.random.randomizer.R
import com.random.randomizer.presentation.core.WheelSegment
import com.random.randomizer.presentation.core.WheelSegmentDefaults
import com.random.randomizer.presentation.core.WheelSegmentUiState
import com.random.randomizer.presentation.navigation.SharedContentKeys
import com.random.randomizer.presentation.theme.RandomizerTheme
import com.random.randomizer.presentation.util.PreviewContainer
import kotlinx.coroutines.delay

@Composable
fun SpinButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    elevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.elevation()
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
        elevation = elevation,
        onClick = onClick,
        modifier = modifier
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onClickSettings: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = Color.Transparent,
    scrolledContainerColor: Color = Color.Unspecified
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = containerColor,
            scrolledContainerColor = scrolledContainerColor
        ),
        title = {
            Text(text = stringResource(R.string.home_screen_title))
        },
        actions = {
            IconButton(onClick = onClickSettings) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = stringResource(R.string.cd_settings)
                )
            }
        },
        scrollBehavior = scrollBehavior,
        modifier = modifier
    )
}

@Preview
@Composable
private fun HomeTopBarPreview() {
    @OptIn(ExperimentalMaterial3Api::class)
    RandomizerTheme {
        Surface {
            HomeTopBar(
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
                onClickSettings = {}
            )
        }
    }
}

@Composable
private fun AddSegmentSmallButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    elevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.elevation()
) {
    SmallFloatingActionButton(
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.secondary,
        elevation = elevation,
        modifier = modifier.size(40.dp) // To align perfectly to the end
    ) {
        AddSegmentIcon()
    }
}

@Composable
private fun AddSegmentButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    elevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.elevation()
) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.secondary,
        elevation = elevation,
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

@Composable
fun FabsColumn(
    state: FabsColumnState,
    onClickSpin: () -> Unit,
    onClickAdd: () -> Unit,
    modifier: Modifier = Modifier,
    animationStepMillis: Int = FabsColumnDefaults.ANIMATION_STEP_MS,
    containerElevation: Dp = FabsColumnDefaults.ContainerElevation
) {
    val windowInsets = WindowInsets.navigationBars
        .union(WindowInsets.displayCutout)
        .only(WindowInsetsSides.Horizontal)

    val transition = updateTransition(state, label = "Home fabs")

    val fadeInAnimation = fadeIn(
        animationSpec = tween(
            durationMillis = animationStepMillis,
            delayMillis = animationStepMillis * 2
        )
    )

    val fadeOutAnimation = fadeOut(
        animationSpec = tween(
            durationMillis = animationStepMillis,
            delayMillis = animationStepMillis
        )
    )

    @Composable
    fun animateElevation(forState: FabsColumnState) = transition.animateDp(
        transitionSpec = {
            if (forState == targetState) {
                tween(durationMillis = animationStepMillis, delayMillis = animationStepMillis * 3)
            } else {
                tween(durationMillis = animationStepMillis)
            }
        }
    ) { state ->
        if (forState == state) containerElevation else 0.dp
    }

    val addButtonElevation by animateElevation(FabsColumnState.OnlyAddButton)
    val addAndSpinButtonsElevation by animateElevation(FabsColumnState.AddAndSpinButton)

    Box(
        modifier = modifier
            .consumeWindowInsets(PaddingValues(16.dp)) // Consume default FabSpacing
            .windowInsetsPadding(windowInsets),
        contentAlignment = Alignment.BottomEnd
    ) {
        transition.AnimatedVisibility(
            visible = { state -> state == FabsColumnState.AddAndSpinButton },
            enter = fadeInAnimation,
            exit = fadeOutAnimation
        ) {
            val elevation = FloatingActionButtonDefaults
                .elevation(defaultElevation = addAndSpinButtonsElevation)

            Column(horizontalAlignment = Alignment.End) {
                AddSegmentSmallButton(onClick = onClickAdd, elevation = elevation)
                Spacer(Modifier.height(16.dp))
                SpinButton(onClick = onClickSpin, elevation = elevation)
            }
        }

        transition.AnimatedVisibility(
            visible = { state -> state == FabsColumnState.OnlyAddButton },
            enter = fadeInAnimation,
            exit = fadeOutAnimation
        ) {
            AddSegmentButton(
                onClick = onClickAdd,
                elevation = FloatingActionButtonDefaults
                    .elevation(defaultElevation = addButtonElevation)
            )
        }
    }
}

enum class FabsColumnState {
    OnlyAddButton,
    AddAndSpinButton
}

object FabsColumnDefaults {
    val ContainerElevation = 6.dp
    const val ANIMATION_STEP_MS = 150
}

@PreviewLightDark
@Composable
private fun FabColumnOnlyAddButtonPreview() {
    PreviewContainer {
        FabsColumn(
            state = FabsColumnState.OnlyAddButton,
            onClickSpin = {},
            onClickAdd = {}
        )
    }
}

@Preview
@Composable
private fun FabColumnBothButtonsPreview() {
    PreviewContainer {
        FabsColumn(
            state = FabsColumnState.AddAndSpinButton,
            onClickSpin = {},
            onClickAdd = {},
        )
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
    if (state.dismissDirection != SwipeToDismissBoxValue.EndToStart) return

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.errorContainer, WheelSegmentDefaults.Shape)
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
fun SharedTransitionScope.DeletableWheelSegmentList(
    animatedVisibilityScope: AnimatedVisibilityScope,
    wheelItems: List<WheelSegmentUiState>,
    onClick: (WheelSegmentUiState) -> Unit,
    onDelete: (WheelSegmentUiState) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        modifier = modifier,
        state = state,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = wheelItems,
            key = { it.id }
        ) { item ->
            DeletableWheelSegment(
                itemUiState = item,
                onDelete = { onDelete(item) },
                onClick = { onClick(item) },
                modifier = Modifier.sharedElement(
                    state = rememberSharedContentState(
                        key = SharedContentKeys.ofSegment(item.id)
                    ),
                    animatedVisibilityScope = animatedVisibilityScope
                )
            )
        }
    }
}

@Composable
fun SharedTransitionScope.DeletableWheelSegmentGrid(
    animatedVisibilityScope: AnimatedVisibilityScope,
    wheelItems: List<WheelSegmentUiState>,
    onClick: (WheelSegmentUiState) -> Unit,
    onDelete: (WheelSegmentUiState) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    state: LazyGridState = rememberLazyGridState()
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(minSize = 256.dp),
        state = state,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = wheelItems,
            key = { it.id }
        ) { item ->
            DeletableWheelSegment(
                itemUiState = item,
                onDelete = { onDelete(item) },
                onClick = { onClick(item) },
                modifier = Modifier.sharedElement(
                    state = rememberSharedContentState(
                        key = SharedContentKeys.ofSegment(item.id)
                    ),
                    animatedVisibilityScope = animatedVisibilityScope
                )
            )
        }
    }
}