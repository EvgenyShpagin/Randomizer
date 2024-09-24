package com.random.randomizer.presentation.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.random.randomizer.presentation.core.WheelItemUiState
import com.random.randomizer.presentation.core.WheelItemsList

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()

    HomeScreen(
        wheelItems = uiState.wheelItems,
        modifier = modifier
    )
}

@Composable
private fun HomeScreen(
    wheelItems: List<WheelItemUiState>,
    listState: LazyListState = rememberLazyListState(),
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            SpinButton(onClick = {}) // TODO: implement
        },
        topBar = {
            HomeTopBar()
        },
        content = { innerPadding ->
            WheelItemsList(
                wheelItems = wheelItems,
                listState = listState,
                modifier = Modifier.padding(innerPadding)
            )
        }
    )
}