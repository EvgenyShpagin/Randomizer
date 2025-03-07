@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class,
    ExperimentalLayoutApi::class
)

package com.random.randomizer.presentation.screen.edit

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.annotation.VisibleForTesting
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.random.randomizer.R
import com.random.randomizer.presentation.core.WheelSegment
import com.random.randomizer.presentation.core.unionPaddingWithInsets
import com.random.randomizer.presentation.screen.edit.EditUiEffect.NavigateBack
import com.random.randomizer.presentation.screen.edit.EditUiEffect.ShowErrorMessage
import com.random.randomizer.presentation.screen.edit.EditUiEvent.FinishEdit
import com.random.randomizer.presentation.screen.edit.EditUiEvent.InputDescription
import com.random.randomizer.presentation.screen.edit.EditUiEvent.InputTitle
import com.random.randomizer.presentation.screen.edit.EditUiEvent.PickColor
import com.random.randomizer.presentation.screen.edit.EditUiEvent.PickImage
import com.random.randomizer.presentation.screen.edit.EditUiEvent.RemoveImage
import com.random.randomizer.presentation.theme.AppTheme
import com.random.randomizer.presentation.util.HandleUiEffects
import com.random.randomizer.presentation.util.PreviewWheelSegmentList


@Composable
fun EditScreen(
    viewModel: EditViewModel,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    val pickMedia = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        viewModel.onEvent(PickImage(context, uri))
    }
    BackHandler {
        viewModel.onEvent(FinishEdit(doSave = false))
    }

    HandleUiEffects(viewModel.uiEffect) { effect ->
        when (effect) {
            NavigateBack -> {
                navigateBack()
            }

            is ShowErrorMessage -> {
                Toast.makeText(context, effect.textId, Toast.LENGTH_SHORT).show()
            }
        }
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            EditTopAppBar(
                onNavigationClick = { viewModel.onEvent(FinishEdit(doSave = false)) },
                scrollBehavior = scrollBehavior
            )
        },
        content = { innerPadding ->
            EditContent(
                uiState = uiState,
                onInputTitle = { title ->
                    viewModel.onEvent(InputTitle(title))
                },
                onInputDescription = { description ->
                    viewModel.onEvent(InputDescription(description))
                },
                onClickAddImage = {
                    pickMedia.launchImagePicker()
                },
                onClickRemoveImage = {
                    viewModel.onEvent(RemoveImage)
                },
                onPickBackgroundColor = { color ->
                    viewModel.onEvent(PickColor(color))
                },
                onSaveClicked = {
                    viewModel.onEvent(FinishEdit(doSave = true))
                },
                modifier = Modifier
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding)
            )
        }
    )
}

@Composable
private fun EditContent(
    uiState: EditUiState,
    onSaveClicked: () -> Unit,
    onInputTitle: (String) -> Unit,
    onInputDescription: (String) -> Unit,
    onClickAddImage: () -> Unit,
    onClickRemoveImage: () -> Unit,
    onPickBackgroundColor: (Color?) -> Unit,
    modifier: Modifier = Modifier,
    backgroundColors: List<Color> = AllSegmentColors
) {
    val scope = rememberCoroutineScope()
    val bringIntoTitleViewRequester = remember { BringIntoViewRequester() }
    val bringIntoDescriptionViewRequester = remember { BringIntoViewRequester() }
    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .unionPaddingWithInsets(
                PaddingValues(16.dp),
                WindowInsets.displayCutout
            ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        HeaderedContent(text = stringResource(R.string.label_preview).uppercase()) {
            WheelSegment(
                itemUiState = uiState.segmentUiState,
                isClickable = false,
                onClick = {}
            )
        }

        // Extra space
        Spacer(modifier = Modifier.height(16.dp))

        HeaderedContent(text = stringResource(R.string.label_details).uppercase()) {
            TitleTextField(
                title = uiState.segmentUiState.title,
                onInput = onInputTitle,
                modifier = Modifier
                    .bringIntoViewRequesterOnFocus(bringIntoTitleViewRequester, scope)
            )
        }

        DescriptionTextField(
            description = uiState.segmentUiState.description,
            onInput = onInputDescription,
            modifier = Modifier
                .bringIntoViewRequesterOnFocus(bringIntoDescriptionViewRequester, scope)
        )
        if (uiState.segmentUiState.image == null) {
            AddSegmentImageButton(onClickAdd = onClickAddImage)
        } else {
            RemoveSegmentImageButton(onClickRemove = onClickRemoveImage)
        }
        SegmentColorsRow(
            colors = backgroundColors,
            onCheckColor = onPickBackgroundColor,
            onRemoveColor = { onPickBackgroundColor(null) },
            checkedColor = uiState.segmentUiState.customColor
        )
        Spacer(Modifier.weight(1f))
        AnimatedVisibility(
            visible = !WindowInsets.isImeVisible,
            enter = fadeIn(),
            exit = ExitTransition.None
        ) {
            SaveButton(
                onClick = onSaveClicked,
                isEnabled = uiState.canSave,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun EditWheelSegmentContentPreview() {
    AppTheme {
        EditContent(
            uiState = EditUiState(
                PreviewWheelSegmentList.first(),
                canSave = true
            ),
            onInputTitle = {},
            onInputDescription = {},
            onClickAddImage = {},
            onClickRemoveImage = {},
            onPickBackgroundColor = {},
            onSaveClicked = {}
        )
    }
}

@VisibleForTesting
val AllSegmentColors = listOf(
    Color(0xFFEF9A9A),
    Color(0xFFF48FB1),
    Color(0xFFCE93D8),
    Color(0xFFB39DDB),
    Color(0xFF9FA8DA),
    Color(0xFF90CAF9),
    Color(0xFF81D4FA),
    Color(0xFF80DEEA),
    Color(0xFF80CBC4),
    Color(0xFFA5D6A7),
    Color(0xFFC5E1A5),
    Color(0xFFE6EE9C),
    Color(0xFFFFF59D),
    Color(0xFFFFE082),
    Color(0xFFFFCC80),
    Color(0xFFFFAB91)
)

private fun ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>.launchImagePicker() {
    launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
}