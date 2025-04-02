package com.random.randomizer.presentation.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape

/**
 * A class to model gradient color values.
 *
 * @param topColor The top gradient color to be rendered.
 * @param bottomColor The bottom gradient color to be rendered.
 * @param container The container that will appear above the gradient.
 */
@Immutable
data class GradientBackground(
    val topColor: Color = Color.Unspecified,
    val bottomColor: Color = Color.Unspecified,
    val container: Background = Background()
)

/**
 * A class to model background placed on [GradientBackground]
 *
 * @param color The container color.
 * @param contentColor The preferred content color provided by this Background to its children.
 * @param shape Defines the background's shape.
 * @param margin Defines container's padding
 */
@Immutable
data class Background(
    val color: Color = Color.Unspecified,
    val contentColor: Color = Color.Unspecified,
    val shape: Shape = RectangleShape,
    val margin: PaddingValues = PaddingValues()
)

/**
 * CompositionLocal containing the preferred Background for a given position in the hierarchy.
 *
 * Should be used by screen-level composables.
 */
val LocalBackground = staticCompositionLocalOf { Background() }