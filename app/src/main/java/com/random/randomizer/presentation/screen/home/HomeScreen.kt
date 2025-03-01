package com.random.randomizer.presentation.screen.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.random.randomizer.presentation.core.NoWheelSegmentsPlaceholder
import com.random.randomizer.presentation.core.WheelSegmentUiState
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
        wheelItems = uiState.wheelSegments,
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
    wheelItems: List<WheelSegmentUiState>,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    topAppBarState: TopAppBarState = rememberTopAppBarState()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topAppBarState)
    Scaffold(
        topBar = {
            HomeTopBar(scrollBehavior = scrollBehavior)
        },
        floatingActionButton = {
            FabsColumn(
                showSpinButton = wheelItems.count() > 1,
                onClickSpin = onClickSpin,
                onClickAdd = onClickAdd
            )
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
                val cutoutWindowInsets = WindowInsets.displayCutout.asPaddingValues()
                val layoutDirection = LocalLayoutDirection.current

                val contentPadding = PaddingValues(
                    start = cutoutWindowInsets
                        .calculateStartPadding(layoutDirection)
                        .coerceAtLeast(16.dp),
                    end = cutoutWindowInsets
                        .calculateEndPadding(layoutDirection)
                        .coerceAtLeast(16.dp),
                    top = 16.dp,
                    bottom = 16.dp
                )

                DeletableWheelSegmentList(
                    wheelItems = wheelItems,
                    onClick = onClickWheelSegment,
                    onDelete = onDeleteWheelSegment,
                    contentPadding = contentPadding,
                    listState = listState
                )
            }
        }
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
                wheelItems = wheelSegments,
            )
        }
    }
}