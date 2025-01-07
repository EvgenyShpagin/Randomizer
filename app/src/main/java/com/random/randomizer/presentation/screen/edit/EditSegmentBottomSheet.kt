package com.random.randomizer.presentation.screen.edit

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.random.randomizer.presentation.core.WheelSegmentUiState
import com.random.randomizer.presentation.screen.edit.EditSegmentUiEvent.InputDescription
import com.random.randomizer.presentation.screen.edit.EditSegmentUiEvent.InputTitle
import com.random.randomizer.presentation.screen.edit.EditSegmentUiEvent.OpenImagePicker
import com.random.randomizer.presentation.screen.edit.EditSegmentUiEvent.PickColor
import com.random.randomizer.presentation.screen.edit.EditSegmentUiEvent.PickImage
import com.random.randomizer.presentation.screen.edit.EditSegmentUiEvent.RemoveImage
import com.random.randomizer.presentation.util.HandleUiEffects


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditSegmentBottomSheet(
    currentlyEditedSegmentId: Int,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditSegmentViewModel = hiltViewModel(
        creationCallback = { factory: EditSegmentViewModel.Factory ->
            factory.create(currentlyEditedSegmentId)
        }
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    val pickMedia = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        viewModel.onEvent(PickImage(context, uri))
    }

    HandleUiEffects(viewModel.uiEffect) { effect ->
        when (effect) {
            EditSegmentUiEffect.OpenImagePicker -> {
                pickMedia.launchImagePicker()
            }

            is EditSegmentUiEffect.ShowErrorMessage -> {
                Toast.makeText(context, effect.textId, Toast.LENGTH_SHORT).show()
            }
        }
    }

    EditSegmentBottomSheet(
        segmentUiState = uiState,
        onDismiss = onDismiss,
        onInputTitle = { title ->
            viewModel.onEvent(InputTitle(title))
        },
        onInputDescription = { description ->
            viewModel.onEvent(InputDescription(description))
        },
        onClickAddImage = {
            viewModel.onEvent(OpenImagePicker)
        },
        onClickRemoveImage = {
            viewModel.onEvent(RemoveImage)
        },
        onPickBackgroundColor = { color ->
            viewModel.onEvent(PickColor(color))
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditSegmentBottomSheet(
    segmentUiState: WheelSegmentUiState,
    onDismiss: () -> Unit,
    onInputTitle: (String) -> Unit,
    onInputDescription: (String) -> Unit,
    onClickAddImage: () -> Unit,
    onClickRemoveImage: () -> Unit,
    onPickBackgroundColor: (Color?) -> Unit,
    modifier: Modifier = Modifier,
    backgroundColors: List<Color> = AllSegmentColors
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        modifier = modifier,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            SegmentTitleTextField(
                title = segmentUiState.title,
                onInput = onInputTitle
            )
            SegmentDescriptionTextField(
                description = segmentUiState.description,
                onInput = onInputDescription
            )
            if (segmentUiState.image == null) {
                AddSegmentImageButton(onClickAdd = onClickAddImage)
            } else {
                RemoveSegmentImageButton(onClickRemove = onClickRemoveImage)
            }
            SegmentColorsRow(
                colors = backgroundColors,
                onCheckColor = onPickBackgroundColor,
                onRemoveColor = { onPickBackgroundColor(null) },
                checkedColor = segmentUiState.customColor
            )
        }
    }
}


private val AllSegmentColors = listOf(
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