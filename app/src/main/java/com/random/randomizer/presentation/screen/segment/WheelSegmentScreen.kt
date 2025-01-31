package com.random.randomizer.presentation.screen.segment

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
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.random.randomizer.presentation.core.WheelSegmentUiState
import com.random.randomizer.presentation.screen.edit.AddSegmentImageButton
import com.random.randomizer.presentation.screen.edit.RemoveSegmentImageButton
import com.random.randomizer.presentation.screen.edit.SegmentColorsRow
import com.random.randomizer.presentation.screen.edit.SegmentDescriptionTextField
import com.random.randomizer.presentation.screen.edit.SegmentTitleTextField
import com.random.randomizer.presentation.screen.segment.WheelSegmentUiEvent.InputDescription
import com.random.randomizer.presentation.screen.segment.WheelSegmentUiEvent.InputTitle
import com.random.randomizer.presentation.screen.segment.WheelSegmentUiEvent.OpenImagePicker
import com.random.randomizer.presentation.screen.segment.WheelSegmentUiEvent.PickColor
import com.random.randomizer.presentation.screen.segment.WheelSegmentUiEvent.RemoveImage
import com.random.randomizer.presentation.util.HandleUiEffects


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WheelSegmentScreen(
    viewModel: WheelSegmentViewModel,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    val pickMedia = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        viewModel.onEvent(WheelSegmentUiEvent.PickImage(context, uri))
    }

    HandleUiEffects(viewModel.uiEffect) { effect ->
        when (effect) {
            WheelSegmentUiEffect.OpenImagePicker -> {
                pickMedia.launchImagePicker()
            }

            WheelSegmentUiEffect.NavigateBack -> {
                navigateBack()
            }

            is WheelSegmentUiEffect.ShowErrorMessage -> {
                Toast.makeText(context, effect.textId, Toast.LENGTH_SHORT).show()
            }
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            EditSegmentTopAppBar(onNavigationClick = { viewModel.onEvent(WheelSegmentUiEvent.FinishEdit) })
        },
        content = { innerPadding ->
            WheelSegmentContent(
                segmentUiState = uiState,
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
                modifier = Modifier.padding(innerPadding)
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WheelSegmentContent(
    segmentUiState: WheelSegmentUiState,
    onInputTitle: (String) -> Unit,
    onInputDescription: (String) -> Unit,
    onClickAddImage: () -> Unit,
    onClickRemoveImage: () -> Unit,
    onPickBackgroundColor: (Color?) -> Unit,
    modifier: Modifier = Modifier,
    backgroundColors: List<Color> = AllSegmentColors
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
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