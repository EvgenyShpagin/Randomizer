@file:OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)

package com.random.randomizer.presentation.screen.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
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
import com.random.randomizer.presentation.util.unionWithWindowInsets
import com.random.randomizer.presentation.screen.home.HomeUiEvent.DeleteSegment
import com.random.randomizer.presentation.util.PreviewContainer
import com.random.randomizer.presentation.util.WheelSegmentListParameterProvider

@Composable
fun SharedTransitionScope.HomeScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    navigateToSpin: () -> Unit,
    navigateToEdit: (Int?) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreen(
        animatedVisibilityScope = animatedVisibilityScope,
        onClickSpin = { navigateToSpin() },
        onClickAdd = { navigateToEdit(null) },
        onClickWheelSegment = { navigateToEdit(it.id) },
        onDeleteWheelSegment = { viewModel.onEvent(DeleteSegment(it)) },
        wheelSegments = uiState.wheelSegments,
        isLoading = uiState.isLoading,
        modifier = modifier
    )
}

@Composable
private fun SharedTransitionScope.HomeScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
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
                    state = if (wheelSegments.count() > 1) {
                        FabsColumnState.AddAndSpinButton
                    } else {
                        FabsColumnState.OnlyAddButton
                    },
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
                .consumeWindowInsets(innerPadding)
                .padding(innerPadding)
        ) {
            HomeContent(
                animatedVisibilityScope = animatedVisibilityScope,
                wheelSegments = wheelSegments,
                onClickWheelSegment = onClickWheelSegment,
                onDeleteWheelSegment = onDeleteWheelSegment,
            )
        }
    }
}

@Composable
private fun SharedTransitionScope.HomeContent(
    animatedVisibilityScope: AnimatedVisibilityScope,
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
            animatedVisibilityScope = animatedVisibilityScope,
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
    PreviewContainer { animatedVisibilityScope ->
        HomeScreen(
            animatedVisibilityScope = animatedVisibilityScope,
            onClickSpin = {},
            onClickAdd = {},
            onClickWheelSegment = {},
            onDeleteWheelSegment = {},
            wheelSegments = wheelSegments,
            isLoading = false
        )
    }
}