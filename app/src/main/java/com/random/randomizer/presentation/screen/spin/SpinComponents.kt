package com.random.randomizer.presentation.screen.spin

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.random.randomizer.presentation.core.WheelSegment
import com.random.randomizer.presentation.core.WheelSegmentUiState
import com.random.randomizer.presentation.core.unionWithWindowInsets

@Composable
fun SpinWheelSegmentList(
    wheelSegments: List<WheelSegmentUiState>,
    lazyListState: LazyListState,
    modifier: Modifier = Modifier
) {
    val contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        .unionWithWindowInsets(
            WindowInsets.displayCutout.only(WindowInsetsSides.Horizontal)
        )

    LazyColumn(
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        userScrollEnabled = false,
        state = lazyListState,
        modifier = modifier
    ) {
        items(items = wheelSegments) { item ->
            WheelSegment(
                itemUiState = item,
                onClick = {},
                isClickable = false
            )
        }
    }
}

@Composable
fun TransformableIndicator(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    indicatorThickness: Dp = TransformIndicatorDefaults.IndicatorThickness,
    transformDurationMillis: Int = TransformIndicatorDefaults.DURATION_MS
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    val transition = updateTransition(isLoading)

    val height by transition.animateDp(
        transitionSpec = { tween(transformDurationMillis) }
    ) { isTargetState ->
        if (isTargetState) screenHeight else indicatorThickness
    }

    val color by transition.animateColor(
        transitionSpec = { tween(transformDurationMillis) }
    ) { isTargetState ->
        if (isTargetState) {
            MaterialTheme.colorScheme.surface
        } else {
            MaterialTheme.colorScheme.outlineVariant
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .background(color)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

object TransformIndicatorDefaults {
    val IndicatorThickness = 2.dp
    const val DURATION_MS = 800
}