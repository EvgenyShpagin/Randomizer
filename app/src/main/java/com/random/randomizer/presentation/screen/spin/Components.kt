package com.random.randomizer.presentation.screen.spin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
        .unionWithWindowInsets(WindowInsets.displayCutout)

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