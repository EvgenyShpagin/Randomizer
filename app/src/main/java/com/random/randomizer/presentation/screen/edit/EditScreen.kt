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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.random.randomizer.presentation.core.NoWheelSegmentsPlaceholder
import com.random.randomizer.presentation.core.WheelSegmentList
import com.random.randomizer.presentation.core.WheelSegmentUiState
import com.random.randomizer.presentation.screen.edit.EditUiEvent.CreateSegment
import com.random.randomizer.presentation.screen.edit.EditUiEvent.EditSegment
import com.random.randomizer.presentation.screen.edit.EditUiEvent.NavigateBack
import com.random.randomizer.presentation.util.HandleUiEffects

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(
    navigateBack: () -> Unit,
    viewModel: EditViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HandleUiEffects(viewModel.uiEffect) { effect ->
        when (effect) {
            EditUiEffect.NavigateToHome -> navigateBack()
        }
    }

    EditScreen(
        wheelSegments = uiState.wheelSegments,
        onAddSegmentClicked = { viewModel.onEvent(CreateSegment) },
        onSegmentClicked = { viewModel.onEvent(EditSegment(it)) },
        navigateBack = { viewModel.onEvent(NavigateBack) },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditScreen(
    wheelSegments: List<WheelSegmentUiState>,
    onAddSegmentClicked: () -> Unit,
    onSegmentClicked: (WheelSegmentUiState) -> Unit,
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
            AddWheelSegmentButton(onClick = onAddSegmentClicked)
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
                    listState = listState,
                    onClickWheelSegment = onSegmentClicked
                )
            }
        }
    }
}