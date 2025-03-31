@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class,
    ExperimentalLayoutApi::class,
    ExperimentalSharedTransitionApi::class
)

package com.random.randomizer.presentation.screen.edit

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.annotation.VisibleForTesting
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
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
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.random.randomizer.R
import com.random.randomizer.presentation.core.ScreenBackground
import com.random.randomizer.presentation.core.StatefulContent
import com.random.randomizer.presentation.core.WheelSegment
import com.random.randomizer.presentation.navigation.SharedContentKeys
import com.random.randomizer.presentation.screen.edit.EditUiEffect.NavigateBack
import com.random.randomizer.presentation.screen.edit.EditUiEffect.ShowErrorMessage
import com.random.randomizer.presentation.screen.edit.EditUiEvent.FinishEdit
import com.random.randomizer.presentation.screen.edit.EditUiEvent.InputDescription
import com.random.randomizer.presentation.screen.edit.EditUiEvent.InputTitle
import com.random.randomizer.presentation.screen.edit.EditUiEvent.PickColor
import com.random.randomizer.presentation.screen.edit.EditUiEvent.PickImage
import com.random.randomizer.presentation.screen.edit.EditUiEvent.RemoveImage
import com.random.randomizer.presentation.util.HandleUiEffects
import com.random.randomizer.presentation.util.PreviewContainer
import com.random.randomizer.presentation.util.PreviewWheelSegmentList
import com.random.randomizer.presentation.util.unionPaddingWithInsets
import kotlinx.coroutines.launch


@Composable
fun SharedTransitionScope.EditScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: EditViewModel,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    enableAnimations: Boolean = true
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    val snackbarHostState = remember { SnackbarHostState() }

    HandleUiEffects(viewModel.uiEffect) { effect ->
        when (effect) {
            NavigateBack -> {
                navigateBack()
            }

            is ShowErrorMessage -> {
                val message = context.getString(effect.textId)
                snackbarHostState.showSnackbar(message, withDismissAction = true)
            }
        }
    }

    EditScreen(
        modifier = modifier,
        animatedVisibilityScope = animatedVisibilityScope,
        isLoading = uiState.isLoading,
        id = uiState.segmentUiState.id,
        title = viewModel.title, // Use Compose State instead of StateFlow
        description = viewModel.description, // to synchronously update TextFields
        customColor = uiState.segmentUiState.customColor,
        image = uiState.segmentUiState.image,
        canSave = uiState.canSave,
        onInputTitle = { title -> viewModel.onEvent(InputTitle(title)) },
        onInputDescription = { description -> viewModel.onEvent(InputDescription(description)) },
        onPickImage = { uri -> viewModel.onEvent(PickImage(context, uri)) },
        onPickBackgroundColor = { color -> viewModel.onEvent(PickColor(color)) },
        onRemoveImage = { viewModel.onEvent(RemoveImage) },
        onDismiss = { viewModel.onEvent(FinishEdit(doSave = false)) },
        onSave = { viewModel.onEvent(FinishEdit(doSave = true)) },
        snackbarHostState = snackbarHostState,
        enableAnimations = enableAnimations
    )
}

@Composable
private fun SharedTransitionScope.EditScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    isLoading: Boolean,
    id: Int,
    title: String,
    description: String,
    customColor: Color?,
    image: ImageBitmap?,
    canSave: Boolean,
    onInputTitle: (String) -> Unit,
    onInputDescription: (String) -> Unit,
    onPickImage: (Uri?) -> Unit,
    onRemoveImage: () -> Unit,
    onPickBackgroundColor: (Color?) -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    enableAnimations: Boolean = true
) {
    BackHandler {
        onDismiss()
    }

    val pickMedia = rememberLauncherForActivityResult(PickVisualMedia(), onPickImage)

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    ScreenBackground(modifier = modifier) { color, contentColor ->
        Scaffold(
            containerColor = color,
            contentColor = contentColor,
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            snackbarHost = {
                SnackbarHost(snackbarHostState)
            },
            topBar = {
                EditTopAppBar(
                    onNavigationClick = { onDismiss() },
                    scrollBehavior = scrollBehavior
                )
            },
            content = { innerPadding ->
                StatefulContent(
                    isLoading = isLoading,
                    modifier = Modifier
                        .padding(innerPadding)
                        .consumeWindowInsets(innerPadding)
                        .fillMaxSize()
                ) {
                    EditContent(
                        animatedVisibilityScope = animatedVisibilityScope,
                        id = id,
                        title = title,
                        description = description,
                        customColor = customColor,
                        image = image,
                        canSave = canSave,
                        onInputTitle = onInputTitle,
                        onInputDescription = onInputDescription,
                        onClickAddImage = { pickMedia.launchImagePicker() },
                        onClickRemoveImage = onRemoveImage,
                        onPickBackgroundColor = onPickBackgroundColor,
                        onSaveClicked = onSave,
                        enableAnimations = enableAnimations
                    )
                }
            }
        )
    }
}

@Composable
private fun SharedTransitionScope.EditContent(
    animatedVisibilityScope: AnimatedVisibilityScope,
    id: Int,
    title: String,
    description: String,
    image: ImageBitmap?,
    customColor: Color?,
    canSave: Boolean,
    onSaveClicked: () -> Unit,
    onInputTitle: (String) -> Unit,
    onInputDescription: (String) -> Unit,
    onClickAddImage: () -> Unit,
    onClickRemoveImage: () -> Unit,
    onPickBackgroundColor: (Color?) -> Unit,
    modifier: Modifier = Modifier,
    backgroundColors: List<Color> = AllSegmentColors,
    enableAnimations: Boolean = true
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
                title = title,
                description = description,
                image = image,
                containerColor = customColor,
                isClickable = false,
                onClick = {},
                modifier = Modifier.sharedElement(
                    state = rememberSharedContentState(
                        key = SharedContentKeys.ofSegment(id)
                    ),
                    animatedVisibilityScope = animatedVisibilityScope
                )
            )
        }

        // Extra space
        Spacer(modifier = Modifier.height(16.dp))

        HeaderedContent(text = stringResource(R.string.label_details).uppercase()) {
            TitleTextField(
                title = title,
                onInput = onInputTitle,
                modifier = Modifier
                    .bringIntoViewRequester(bringIntoTitleViewRequester)
                    .onFocusChanged { state ->
                        if (state.isFocused) {
                            scope.launch {
                                bringIntoTitleViewRequester.bringIntoView()
                            }
                        }
                    }
            )
        }

        DescriptionTextField(
            description = description,
            onInput = onInputDescription,
            modifier = Modifier
                .bringIntoViewRequester(bringIntoDescriptionViewRequester)
                .onFocusChanged { state ->
                    if (state.isFocused) {
                        scope.launch {
                            bringIntoDescriptionViewRequester.bringIntoView()
                        }
                    }
                }
        )
        if (image == null) {
            AddSegmentImageButton(onClickAdd = onClickAddImage)
        } else {
            RemoveSegmentImageButton(onClickRemove = onClickRemoveImage)
        }
        SegmentColorsRow(
            colors = backgroundColors,
            onCheckColor = onPickBackgroundColor,
            onRemoveColor = { onPickBackgroundColor(null) },
            checkedColor = customColor
        )
        Spacer(Modifier.weight(1f))
        if (enableAnimations) {
            AnimatedVisibility(
                visible = !WindowInsets.isImeVisible,
                enter = fadeIn(),
                exit = ExitTransition.None
            ) {
                SaveButton(
                    onClick = onSaveClicked,
                    isEnabled = canSave,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        } else {
            SaveButton(
                onClick = onSaveClicked,
                isEnabled = canSave,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun EditScreenPreview() {
    PreviewContainer { animatedVisibilityScope ->
        val wheelSegment = PreviewWheelSegmentList.first()
        EditScreen(
            animatedVisibilityScope = animatedVisibilityScope,
            isLoading = false,
            id = wheelSegment.id,
            title = wheelSegment.title,
            description = wheelSegment.description,
            image = wheelSegment.image,
            customColor = wheelSegment.customColor,
            canSave = true,
            onInputTitle = {},
            onInputDescription = {},
            onPickImage = {},
            onRemoveImage = {},
            onPickBackgroundColor = {},
            snackbarHostState = SnackbarHostState(),
            onSave = {},
            onDismiss = {}
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