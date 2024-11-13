package com.random.randomizer.presentation.screen.spin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import com.random.randomizer.presentation.core.WheelSegment
import com.random.randomizer.presentation.core.WheelSegmentUiState

@Composable
fun SpinWheelSegmentList(
    wheelSegments: List<WheelSegmentUiState>,
    lazyListState: LazyListState,
    modifier: Modifier = Modifier
) {
    val layoutDirection = LocalLayoutDirection.current
    val displayCutout = WindowInsets.displayCutout.asPaddingValues()
    val cutoutStartPadding = displayCutout.calculateStartPadding(layoutDirection)
    val cutoutEndPadding = displayCutout.calculateEndPadding(layoutDirection)

    val safeContentPadding = WindowInsets.safeContent.asPaddingValues()
    val topPadding = safeContentPadding.calculateTopPadding()
    val bottomPadding = safeContentPadding.calculateBottomPadding()

    LazyColumn(
        contentPadding = PaddingValues(
            start = cutoutStartPadding.coerceAtLeast(16.dp),
            end = cutoutEndPadding.coerceAtLeast(16.dp),
            top = 8.dp,
            bottom = 8.dp
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        userScrollEnabled = false,
        state = lazyListState,
        modifier = modifier.padding(
            top = topPadding,
            bottom = bottomPadding
        )
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