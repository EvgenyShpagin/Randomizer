package com.random.randomizer.presentation.screen.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.random.randomizer.presentation.core.NoWheelSegmentsPlaceholder
import com.random.randomizer.presentation.core.StatefulContent
import com.random.randomizer.presentation.core.WheelSegmentUiState
import com.random.randomizer.presentation.core.unionWithWindowInsets
import com.random.randomizer.presentation.screen.home.HomeUiEvent.DeleteSegment
import com.random.randomizer.presentation.theme.AppTheme
import com.random.randomizer.presentation.util.WheelSegmentListParameterProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToSpin: () -> Unit,
    navigateToEdit: (Int?) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreen(
        onClickSpin = { navigateToSpin() },
        onClickAdd = { navigateToEdit(null) },
        onClickWheelSegment = { navigateToEdit(it.id) },
        onDeleteWheelSegment = { viewModel.onEvent(DeleteSegment(it)) },
        wheelSegments = uiState.wheelSegments,
        isLoading = uiState.isLoading,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    onClickSpin: () -> Unit,
    onClickAdd: () -> Unit,
    onClickWheelSegment: (WheelSegmentUiState) -> Unit,
    onDeleteWheelSegment: (WheelSegmentUiState) -> Unit,
    wheelSegments: List<WheelSegmentUiState>,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    topAppBarState: TopAppBarState = rememberTopAppBarState()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topAppBarState)
    Scaffold(
        topBar = {
            HomeTopBar(scrollBehavior = scrollBehavior)
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = !isLoading,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                FabsColumn(
                    showSpinButton = wheelSegments.count() > 1,
                    onClickSpin = onClickSpin,
                    onClickAdd = onClickAdd
                )
            }
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        StatefulContent(
            isLoading = isLoading,
            isEmpty = wheelSegments.isEmpty(),
            emptyStateContent = {
                NoWheelSegmentsPlaceholder(Modifier.fillMaxSize())
            },
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(innerPadding)
                .padding(innerPadding)
        ) {
            HomeContent(
                wheelSegments = wheelSegments,
                onClickWheelSegment = onClickWheelSegment,
                onDeleteWheelSegment = onDeleteWheelSegment,
            )
        }
    }
}

@Composable
private fun HomeContent(
    wheelSegments: List<WheelSegmentUiState>,
    onClickWheelSegment: (WheelSegmentUiState) -> Unit,
    onDeleteWheelSegment: (WheelSegmentUiState) -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState()
) {
    Box(modifier = modifier) {
        val contentPadding = PaddingValues(16.dp)
            .unionWithWindowInsets(
                WindowInsets.displayCutout.only(WindowInsetsSides.Horizontal)
            )
        DeletableWheelSegmentList(
            wheelItems = wheelSegments,
            onClick = onClickWheelSegment,
            onDelete = onDeleteWheelSegment,
            contentPadding = contentPadding,
            listState = listState
        )
    }
}

@PreviewLightDark
@Composable
private fun HomeScreenPreview(
    @PreviewParameter(WheelSegmentListParameterProvider::class)
    wheelSegments: List<WheelSegmentUiState>
) {
    @OptIn(ExperimentalMaterial3Api::class)
    AppTheme {
        Surface {
            HomeScreen(
                onClickSpin = {},
                onClickAdd = {},
                onClickWheelSegment = {},
                onDeleteWheelSegment = {},
                wheelSegments = wheelSegments,
                isLoading = false
            )
        }
    }
}