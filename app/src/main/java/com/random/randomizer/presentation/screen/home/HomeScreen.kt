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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.random.randomizer.presentation.core.NoWheelSegmentsPlaceholder
import com.random.randomizer.presentation.core.WheelSegmentList
import com.random.randomizer.presentation.core.WheelSegmentUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToSpin: () -> Unit,
    navigateToEdit: () -> Unit,
    homeViewModel: HomeViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()

    HomeScreen(
        navigateToSpin = navigateToSpin,
        navigateToEdit = navigateToEdit,
        wheelItems = uiState.wheelSegments,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    navigateToSpin: () -> Unit,
    navigateToEdit: () -> Unit,
    wheelItems: List<WheelSegmentUiState>,
    listState: LazyListState = rememberLazyListState(),
    topAppBarState: TopAppBarState = rememberTopAppBarState(),
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topAppBarState)
    Scaffold(
        topBar = {
            HomeTopBar(
                onClickEdit = navigateToEdit,
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            if (wheelItems.isNotEmpty()) {
                SpinButton(onClick = navigateToSpin)
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
                    listState = listState
                )
            }
        }
    }
}