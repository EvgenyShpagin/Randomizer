package com.random.randomizer.presentation.screen.home

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.random.randomizer.presentation.core.WheelSegmentList
import com.random.randomizer.presentation.core.WheelSegmentUiState

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()

    HomeScreen(
        wheelItems = uiState.wheelSegments,
        modifier = modifier
    )
}

@Composable
private fun HomeScreen(
    wheelItems: List<WheelSegmentUiState>,
    listState: LazyListState = rememberLazyListState(),
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            HomeTopBar()
        },
        floatingActionButton = {
            SpinButton(onClick = {}) // TODO: implement
        },
        modifier = modifier
    ) { innerPadding ->
        WheelSegmentList(
            wheelItems = wheelItems,
            listState = listState,
            modifier = Modifier.padding(innerPadding)
        )
    }
}