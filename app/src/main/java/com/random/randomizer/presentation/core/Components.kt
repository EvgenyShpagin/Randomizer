package com.random.randomizer.presentation.core

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowDpSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass
import com.random.randomizer.R
import com.random.randomizer.presentation.theme.Background
import com.random.randomizer.presentation.theme.GradientBackground
import com.random.randomizer.presentation.theme.LocalBackground
import com.random.randomizer.presentation.util.MediumBreakpoint
import com.random.randomizer.presentation.util.areSidesAtLeastMedium
import com.random.randomizer.presentation.util.supportsTransparentNavigationBar

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
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium
                )
            },
            supportingContent = if (description.isNotBlank()) {
                {
                    Text(
                        text = description,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
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
    val Shape @Composable get() = CardDefaults.outlinedShape
}

@Composable
fun StatefulContent(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    isEmpty: Boolean = false,
    loadingStateContent: @Composable BoxScope.() -> Unit = {
        DefaultLoadingStateContent(Modifier.align(Alignment.Center))
    },
    emptyStateContent: @Composable BoxScope.() -> Unit = {
        DefaultEmptyStateContent(Modifier.align(Alignment.Center))
    },
    content: @Composable () -> Unit
) {
    Crossfade(targetState = isLoading to isEmpty, modifier = modifier) { (isLoading, isEmpty) ->
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                isLoading -> loadingStateContent()
                isEmpty -> emptyStateContent()
                else -> content()
            }
        }
    }
}

@Composable
private fun DefaultLoadingStateContent(modifier: Modifier = Modifier) {
    CircularProgressIndicator(modifier = modifier)
}

@Composable
private fun DefaultEmptyStateContent(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.label_default_empty_state),
        modifier = modifier
    )
}

/**
 * Root Randomizer container
 */
@Composable
fun RandomizerBackground(
    modifier: Modifier = Modifier,
    gradientBackground: GradientBackground = RandomizerBackground,
    content: @Composable () -> Unit
) {
    val brush = Brush.verticalGradient(
        colors = listOf(gradientBackground.topColor, gradientBackground.bottomColor)
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush)
            .padding(gradientBackground.container.margin)
            .consumeWindowInsets(gradientBackground.container.margin)
    ) {
        CompositionLocalProvider(LocalBackground provides gradientBackground.container) {
            content()
        }
    }
}

/**
 * Default root background of Randomizer
 */
val RandomizerBackground: GradientBackground
    @Composable get() {
        val colorScheme = MaterialTheme.colorScheme

        val container = Background(
            color = colorScheme.surfaceContainerLowest.copy(alpha = 0.7f),
            contentColor = colorScheme.onSurface,
            shape = ShapeDefaults.Large,
            margin = calculateWindowPaddings()
        )
        return GradientBackground(
            topColor = colorScheme.primaryContainer,
            bottomColor = colorScheme.secondaryContainer,
            container = container
        )
    }

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun calculateWindowPaddings(
    insets: WindowInsets = WindowInsets.systemBars.union(WindowInsets.displayCutout),
    maxContainerWidth: Dp = WindowWidthSizeClass.MediumBreakpoint
): PaddingValues {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val windowIsAtLeastMedium = windowSizeClass.areSidesAtLeastMedium()

    val marginDp = if (windowIsAtLeastMedium) 24.dp else 8.dp

    val insetsPadding = insets.asPaddingValues()
    val layoutDirection = LocalLayoutDirection.current

    var topPadding = insetsPadding.calculateTopPadding()
    var bottomPadding = insetsPadding.calculateBottomPadding()
    var startPadding = insetsPadding.calculateStartPadding(layoutDirection).coerceAtLeast(marginDp)
    var endPadding = insetsPadding.calculateEndPadding(layoutDirection).coerceAtLeast(marginDp)

    val navigationBarSide = getNavigationBarSide()

    if (supportsTransparentNavigationBar()) {
        if (navigationBarSide != WindowInsetsSides.Bottom) {
            // Add topPaddingDp to the bottom to establish vertical
            // symmetry when navigation bar is placed horizontally
            bottomPadding += topPadding
        }
    } else {
        when (navigationBarSide) {
            // Make symmetry on horizontally placed navigation bar
            WindowInsetsSides.Start -> {
                startPadding += marginDp
                bottomPadding += topPadding
            }

            WindowInsetsSides.End -> {
                endPadding += marginDp
                bottomPadding += topPadding
            }

            // Only do vertical symmetry on large screens, as the topPaddingDp
            // above the navigation bar takes up a lot of space on compact screens.
            else -> {
                bottomPadding += if (windowIsAtLeastMedium) topPadding else marginDp
            }
        }
    }
    // Limit the width of the container by adding padding
    if (windowIsAtLeastMedium) {
        val width = currentWindowDpSize().width
        val horizontalPadding = (width - maxContainerWidth) / 2
        startPadding = horizontalPadding.coerceAtLeast(marginDp)
        endPadding = horizontalPadding.coerceAtLeast(marginDp)
    }
    return PaddingValues(
        start = startPadding,
        top = topPadding,
        end = endPadding,
        bottom = bottomPadding,
    )
}

@Composable
private fun getNavigationBarSide(): WindowInsetsSides {
    val layoutDirection = LocalLayoutDirection.current
    val navigationBarPadding = WindowInsets.navigationBars.asPaddingValues()
    if (navigationBarPadding.calculateStartPadding(layoutDirection) != 0.dp) {
        return WindowInsetsSides.Start
    }
    if (navigationBarPadding.calculateEndPadding(layoutDirection) != 0.dp) {
        return WindowInsetsSides.End
    }
    return WindowInsetsSides.Bottom
}

@Composable
fun ScreenBackground(
    modifier: Modifier = Modifier,
    background: Background = LocalBackground.current,
    content: @Composable (backgroundColor: Color, contentColor: Color) -> Unit
) {
    Surface(
        color = Color.Unspecified,
        contentColor = Color.Unspecified,
        shape = background.shape,
        modifier = modifier
    ) {
        content(background.color, background.contentColor)
    }
}