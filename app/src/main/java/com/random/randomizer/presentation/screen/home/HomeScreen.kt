package com.random.randomizer.presentation.screen.home

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
import com.random.randomizer.presentation.core.NoWheelSegmentsPlaceholder
import com.random.randomizer.presentation.core.WheelSegmentList
import com.random.randomizer.presentation.core.WheelSegmentUiState

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

@Composable
private fun HomeScreen(
    navigateToSpin: () -> Unit,
    navigateToEdit: () -> Unit,
    wheelItems: List<WheelSegmentUiState>,
    listState: LazyListState = rememberLazyListState(),
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            HomeTopBar(onClickEdit = navigateToEdit)
        },
        floatingActionButton = {
            if (wheelItems.isNotEmpty()) {
                SpinButton(onClick = navigateToSpin)
            }
        },
        modifier = modifier
    ) { innerPadding ->
        if (wheelItems.isEmpty()) {
            NoWheelSegmentsPlaceholder(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        } else {
            WheelSegmentList(
                wheelItems = wheelItems,
                listState = listState,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}