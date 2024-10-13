package com.random.randomizer.presentation.screen.edit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.random.randomizer.presentation.core.NoWheelSegmentsPlaceholder
import com.random.randomizer.presentation.core.WheelSegmentList
import com.random.randomizer.presentation.core.WheelSegmentUiState

@OptIn(ExperimentalMaterial3Api::class)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditScreen(
    wheelSegments: List<WheelSegmentUiState>,
    onClickAddSegment: () -> Unit,
    navigateBack: () -> Unit,
    listState: LazyListState = rememberLazyListState(),
    topAppBarState: TopAppBarState = rememberTopAppBarState(),
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topAppBarState)
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            EditTopAppBar(
                onNavigationClick = navigateBack,
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            AddWheelSegmentButton(onClick = onClickAddSegment)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .consumeWindowInsets(innerPadding)
                .padding(innerPadding)
        ) {
            if (wheelSegments.isEmpty()) {
                NoWheelSegmentsPlaceholder(
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                WheelSegmentList(
                    wheelItems = wheelSegments,
                    listState = listState
                )
            }
        }
    }
}