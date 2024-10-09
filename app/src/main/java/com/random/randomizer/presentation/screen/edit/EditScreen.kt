package com.random.randomizer.presentation.screen.edit

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.random.randomizer.presentation.core.NoWheelSegmentsPlaceholder
import com.random.randomizer.presentation.core.WheelSegmentList
import com.random.randomizer.presentation.core.WheelSegmentUiState

@Composable
fun EditScreen(
    onClickAddSegment: () -> Unit,
    navigateBack: () -> Unit,
    editViewModel: EditViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by editViewModel.uiState.collectAsStateWithLifecycle()

    EditScreen(
        wheelSegments = uiState.wheelSegments,
        onClickAddSegment = onClickAddSegment,
        navigateBack = navigateBack,
        modifier = modifier
    )
}

@Composable
private fun EditScreen(
    wheelSegments: List<WheelSegmentUiState>,
    onClickAddSegment: () -> Unit,
    navigateBack: () -> Unit,
    listState: LazyListState = rememberLazyListState(),
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            EditTopAppBar(onNavigationClick = navigateBack)
        },
        floatingActionButton = {
            AddWheelSegmentButton(onClick = onClickAddSegment)
        }
    ) { innerPadding ->
        if (wheelSegments.isEmpty()) {
            NoWheelSegmentsPlaceholder(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            )
        }
        WheelSegmentList(
            wheelItems = wheelSegments,
            listState = listState,
            modifier = Modifier.padding(innerPadding)
        )
    }
}