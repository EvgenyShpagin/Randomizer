package com.random.randomizer.presentation.screen.home

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
import com.random.randomizer.presentation.util.HandleUiEffects

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToSpin: () -> Unit,
    navigateToEdit: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HandleUiEffects(viewModel.uiEffect) { effect ->
        when (effect) {
            HomeUiEffect.NavigateToEdit -> navigateToEdit()
            HomeUiEffect.NavigateToSpin -> navigateToSpin()
        }
    }

    HomeScreen(
        onClickSpin = { viewModel.onEvent(HomeUiEvent.Spin) },
        onClickEdit = { viewModel.onEvent(HomeUiEvent.Edit) },
        wheelItems = uiState.wheelSegments,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    onClickSpin: () -> Unit,
    onClickEdit: () -> Unit,
    wheelItems: List<WheelSegmentUiState>,
    listState: LazyListState = rememberLazyListState(),
    topAppBarState: TopAppBarState = rememberTopAppBarState(),
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topAppBarState)
    Scaffold(
        topBar = {
            HomeTopBar(
                onClickEdit = onClickEdit,
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            if (wheelItems.isNotEmpty()) {
                SpinButton(onClick = onClickSpin)
            }
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .consumeWindowInsets(innerPadding)
                .padding(innerPadding)
        ) {
            if (wheelItems.isEmpty()) {
                NoWheelSegmentsPlaceholder(
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                WheelSegmentList(
                    wheelItems = wheelItems,
                    onClickWheelSegment = {},
                    listState = listState
                )
            }
        }
    }
}