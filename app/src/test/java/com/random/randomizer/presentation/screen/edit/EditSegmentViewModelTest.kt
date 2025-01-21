package com.random.randomizer.presentation.screen.edit

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import com.random.randomizer.MainCoroutineRule
import com.random.randomizer.data.repository.FakeWheelSegmentRepository
import com.random.randomizer.domain.model.Image
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.usecase.GetWheelSegmentStreamUseCase
import com.random.randomizer.domain.usecase.UpdateWheelSegmentUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private const val SEGMENT_ID = 1
private val EmptyWheelSegment = WheelSegment(SEGMENT_ID, "", "", null, null)

@OptIn(ExperimentalCoroutinesApi::class)
class EditSegmentViewModelTest {

    // Set the main coroutines dispatcher for unit testing.
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var wheelSegmentRepository: FakeWheelSegmentRepository

    // Subject under test
    private lateinit var viewModel: EditSegmentViewModel

    val mappers = FakeEditSegmentMappers

    @Before
    fun setup() {
        wheelSegmentRepository = FakeWheelSegmentRepository()
        viewModel = EditSegmentViewModel(
            SEGMENT_ID,
            GetWheelSegmentStreamUseCase(wheelSegmentRepository),
            UpdateWheelSegmentUseCase(wheelSegmentRepository),
            mappers
        )
    }

    @Test
    fun updatesState_whenCreated() = runTest {
        // Given - blank wheel segment
        wheelSegmentRepository.add(EmptyWheelSegment)

        // When - on startup

        val expected = mappers.toPresentation(EmptyWheelSegment)
        val actual = viewModel.uiState.value

        // Then - verify state is "blank"
        assertEquals(expected, actual)
    }

    @Test
    fun updatesTitle_onInput() = runTest {
        // Given - blank wheel segment
        wheelSegmentRepository.add(EmptyWheelSegment)

        // When - on title input
        viewModel.onEvent(EditSegmentUiEvent.InputTitle("User input string"))

        val expected = "User input string"
        val actual = viewModel.uiState.value.title

        // Then - verify segment's title was updated
        assertEquals(expected, actual)
    }

    @Test
    fun updatesDescription_onInput() = runTest {
        // Given - blank wheel segment
        wheelSegmentRepository.add(EmptyWheelSegment)

        // When - on description input
        viewModel.onEvent(EditSegmentUiEvent.InputDescription("User input string"))

        val expected = "User input string"
        val actual = viewModel.uiState.value.description

        // Then - verify segment's description was updated
        assertEquals(expected, actual)
    }

    @Test
    fun updatesColor_onPick() = runTest {
        // Given - blank wheel segment
        wheelSegmentRepository.add(EmptyWheelSegment)

        // When - on color pick
        viewModel.onEvent(EditSegmentUiEvent.PickColor(Color.Red))

        val expected = Color.Red
        val actual = viewModel.uiState.value.customColor

        // Then - verify segment's color was updated
        assertEquals(expected, actual)
    }

    @Test
    fun updatesImage_onRemove() = runTest {
        // Given - wheel segment with thumbnail
        val wheelSegment = EmptyWheelSegment.copy(thumbnail = Image("id1", ByteArray(4)))
        wheelSegmentRepository.add(wheelSegment)

        // When - on remove thumbnail
        viewModel.onEvent(EditSegmentUiEvent.RemoveImage)

        val expected: ImageBitmap? = null
        val actual = viewModel.uiState.value.image

        // Then - verify segment's thumbnail was removed
        assertEquals(expected, actual)
    }
}