package com.random.randomizer.presentation.screen.edit

import android.content.Context
import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.random.randomizer.MainCoroutineRule
import com.random.randomizer.data.repository.FakeWheelSegmentRepository
import com.random.randomizer.data.util.ImageScalerImpl
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.usecase.CreateWheelSegmentUseCase
import com.random.randomizer.domain.usecase.FixThumbnailUseCase
import com.random.randomizer.domain.usecase.FixWheelSegmentUseCase
import com.random.randomizer.domain.usecase.GetWheelSegmentUseCase
import com.random.randomizer.domain.usecase.UpdateWheelSegmentUseCase
import com.random.randomizer.domain.usecase.ValidateWheelSegmentUseCase
import com.random.randomizer.presentation.core.FakeCoreMappers
import com.random.randomizer.presentation.navigation.Destination
import com.random.randomizer.presentation.screen.segment.EditWheelSegmentUiEvent.FinishEdit
import com.random.randomizer.presentation.screen.segment.EditWheelSegmentUiEvent.InputDescription
import com.random.randomizer.presentation.screen.segment.EditWheelSegmentUiEvent.InputTitle
import com.random.randomizer.presentation.screen.segment.EditWheelSegmentUiEvent.PickColor
import com.random.randomizer.presentation.screen.segment.EditWheelSegmentUiEvent.PickImage
import com.random.randomizer.presentation.screen.segment.EditWheelSegmentUiEvent.RemoveImage
import com.random.randomizer.presentation.screen.segment.EditWheelSegmentUiState
import com.random.randomizer.presentation.screen.segment.EditWheelSegmentViewModel
import com.random.randomizer.util.getUniqueFilename
import com.random.randomizer.util.toByteArray
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File

private const val SEGMENT_ID = 1
private val EmptyWheelSegment = WheelSegment(SEGMENT_ID, "", "", null, null)

@OptIn(ExperimentalCoroutinesApi::class)
class EditWheelSegmentViewModelTest {

    // Set the main coroutines dispatcher for unit testing.
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var wheelSegmentRepository: FakeWheelSegmentRepository

    // Subject under test
    private lateinit var viewModel: EditWheelSegmentViewModel

    private val mappers = FakeEditSegmentMappers

    @Before
    fun setup() {
        wheelSegmentRepository = FakeWheelSegmentRepository()
        val savedStateHandle = SavedStateHandle().apply {
            mockkToRoute(Destination.EditWheelSegment(SEGMENT_ID))
        }
        viewModel = EditWheelSegmentViewModel(
            savedStateHandle,
            GetWheelSegmentUseCase(wheelSegmentRepository),
            FixWheelSegmentUseCase(),
            CreateWheelSegmentUseCase(FixThumbnailUseCase(), wheelSegmentRepository),
            ValidateWheelSegmentUseCase(wheelSegmentRepository),
            UpdateWheelSegmentUseCase(
                FixThumbnailUseCase(),
                wheelSegmentRepository,
                ImageScalerImpl()
            ),
            mappers
        )
    }

    @Test
    fun updatesState_whenCreated() = runTest {
        // Given - blank wheel segment
        wheelSegmentRepository.add(EmptyWheelSegment)

        // When - on startup

        val expected = EditWheelSegmentUiState(
            segmentUiState = FakeCoreMappers.toPresentation(EmptyWheelSegment),
            canSave = false
        )
        val actual = viewModel.uiState.first()

        // Then - verify state is "blank"
        assertEquals(expected, actual)
    }

    @Test
    fun updatesTitle_onInput() = runTest {
        // Given - blank wheel segment
        wheelSegmentRepository.add(EmptyWheelSegment)

        // Setup initial UI
        viewModel.uiState.first()

        // When - on title input
        viewModel.onEvent(InputTitle("User input string"))

        val expected = "User input string"
        val actual = viewModel.uiState.first().segmentUiState.title

        // Then - verify segment's title was updated
        assertEquals(expected, actual)
    }

    @Test
    fun updatesDescription_onInput() = runTest {
        // Given - blank wheel segment
        wheelSegmentRepository.add(EmptyWheelSegment)

        // Setup initial UI
        viewModel.uiState.first()

        // When - on description input
        viewModel.onEvent(InputDescription("User input string"))

        val expected = "User input string"
        val actual = viewModel.uiState.first().segmentUiState.description

        // Then - verify segment's description was updated
        assertEquals(expected, actual)
    }

    @Test
    fun updatesColor_onPick() = runTest {
        // Given - blank wheel segment
        wheelSegmentRepository.add(EmptyWheelSegment)

        // Setup initial UI
        viewModel.uiState.first()

        // When - on color pick
        viewModel.onEvent(PickColor(Color.Red))

        val expected = Color.Red
        val actual = viewModel.uiState.first().segmentUiState.customColor

        // Then - verify segment's color was updated
        assertEquals(expected, actual)
    }

    @Test
    fun updatesImage_onRemove() = runTest {
        // Given - wheel segment with thumbnail
        wheelSegmentRepository.add(EmptyWheelSegment)

        // Setup initial UI
        viewModel.uiState.first()

        mockkStatic("com.random.randomizer.util.UriUtilsKt")

        val context = mockk<Context>()
        every { context.cacheDir } returns File("\\cacheDir\\")

        val imageUri = mockk<Uri>()
        every { imageUri.getUniqueFilename() } returns "imageId"
        every { imageUri.toByteArray(any()) } returns ByteArray(4)

        viewModel.onEvent(PickImage(context, imageUri))

        // When - on remove thumbnail
        viewModel.onEvent(RemoveImage)

        val expected: ImageBitmap? = null
        val actual = viewModel.uiState.first().segmentUiState.image

        // Then - verify segment's thumbnail was removed
        assertEquals(expected, actual)
    }

    @Test
    fun savesWheelSegment_onFinishWithSave() = runTest {
        // Given - wheel segment
        wheelSegmentRepository.add(EmptyWheelSegment)

        // Setup initial UI
        viewModel.uiState.first()

        // When - on finish WITH save after some edit
        viewModel.onEvent(InputTitle("Changed title"))
        viewModel.onEvent(FinishEdit(doSave = true))

        val expected = EmptyWheelSegment.copy(title = "Changed title")
        val actual = wheelSegmentRepository.get(SEGMENT_ID)

        // Then - verify saved wheel segment changed
        assertEquals(expected, actual)
    }

    @Test
    fun doesNotSave_onFinishWithoutSave() = runTest {
        // Given - wheel segment
        wheelSegmentRepository.add(EmptyWheelSegment)

        // Setup initial UI
        viewModel.uiState.first()

        // When - on finish WITHOUT save after some edit
        viewModel.onEvent(InputTitle("Changed title"))
        viewModel.onEvent(FinishEdit(doSave = false))

        val expected = EmptyWheelSegment
        val actual = wheelSegmentRepository.get(SEGMENT_ID)

        // Then - verify saved wheel segment changed
        assertEquals(expected, actual)
    }

    private inline fun <reified T : Destination> SavedStateHandle.mockkToRoute(route: T) {
        mockkStatic("androidx.navigation.SavedStateHandleKt")
        every { toRoute<T>() } returns route
    }
}