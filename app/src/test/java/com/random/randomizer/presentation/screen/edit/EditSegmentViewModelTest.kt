package com.random.randomizer.presentation.screen.edit

import androidx.compose.ui.graphics.Color
import com.random.randomizer.MainCoroutineRule
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.usecase.DeleteThumbnailUseCase
import com.random.randomizer.domain.usecase.GetWheelSegmentStreamUseCase
import com.random.randomizer.domain.usecase.SaveImageThumbnailUseCase
import com.random.randomizer.domain.usecase.UpdateWheelSegmentUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

private const val SEGMENT_ID = 1
private val EmptyWheelSegment = WheelSegment(1, "", "", null, null)

@OptIn(ExperimentalCoroutinesApi::class)
class EditSegmentViewModelTest {

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    lateinit var viewModel: EditSegmentViewModel

    val getWheelSegmentStreamUseCase: GetWheelSegmentStreamUseCase = mockk()
    val updateWheelSegmentUseCase: UpdateWheelSegmentUseCase = mockk()
    val saveImageThumbnailUseCase: SaveImageThumbnailUseCase = mockk()
    val deleteThumbnailUseCase: DeleteThumbnailUseCase = mockk()

    val mappers = FakeEditSegmentMappers

    private fun initViewModel() {
        viewModel = EditSegmentViewModel(
            SEGMENT_ID,
            getWheelSegmentStreamUseCase,
            updateWheelSegmentUseCase,
            saveImageThumbnailUseCase,
            deleteThumbnailUseCase,
            mappers
        )
    }

    @Test
    fun editViewModel_updatesState_whenCreated() = runTest {
        every { getWheelSegmentStreamUseCase(SEGMENT_ID) } returns flow { emit(EmptyWheelSegment) }

        initViewModel()

        advanceUntilIdle()

        val expected = mappers.toPresentation(EmptyWheelSegment)
        val actual = viewModel.uiState.value

        assertEquals(expected, actual)
    }

    @Test
    fun editViewModel_updatesTitle_onInput() = runTest {
        every { getWheelSegmentStreamUseCase(SEGMENT_ID) } returns flow { emit(EmptyWheelSegment) }

        val segmentTransformSlot = slot<(WheelSegment) -> WheelSegment>()
        coEvery {
            updateWheelSegmentUseCase(
                wheelSegmentId = EmptyWheelSegment.id,
                transform = capture(segmentTransformSlot)
            )
        } just runs

        initViewModel()

        viewModel.onEvent(EditSegmentUiEvent.InputTitle("User input string"))

        advanceUntilIdle()

        val expectedUpdatedSegment = EmptyWheelSegment.copy(title = "User input string")
        val actualUpdatedSegment = segmentTransformSlot.captured(EmptyWheelSegment)

        assertEquals(expectedUpdatedSegment, actualUpdatedSegment)
    }

    @Test
    fun editViewModel_updatesDescription_onInput() = runTest {
        every { getWheelSegmentStreamUseCase(SEGMENT_ID) } returns flow { emit(EmptyWheelSegment) }

        val segmentTransformSlot = slot<(WheelSegment) -> WheelSegment>()
        coEvery {
            updateWheelSegmentUseCase(
                wheelSegmentId = EmptyWheelSegment.id,
                transform = capture(segmentTransformSlot)
            )
        } just runs

        initViewModel()

        viewModel.onEvent(EditSegmentUiEvent.InputDescription("User input string"))

        advanceUntilIdle()

        val expectedUpdatedSegment = EmptyWheelSegment.copy(description = "User input string")
        val actualUpdatedSegment = segmentTransformSlot.captured(EmptyWheelSegment)

        assertEquals(expectedUpdatedSegment, actualUpdatedSegment)
    }

    @Test
    fun editViewModel_updatesColor_onPick() = runTest {
        every { getWheelSegmentStreamUseCase(SEGMENT_ID) } returns flow { emit(EmptyWheelSegment) }

        val segmentTransformSlot = slot<(WheelSegment) -> WheelSegment>()
        coEvery {
            updateWheelSegmentUseCase(
                wheelSegmentId = EmptyWheelSegment.id,
                transform = capture(segmentTransformSlot)
            )
        } just runs

        initViewModel()

        viewModel.onEvent(EditSegmentUiEvent.PickColor(Color.Red))

        advanceUntilIdle()

        val expectedUpdatedSegment =
            EmptyWheelSegment.copy(customColor = mappers.toDomain(Color.Red))
        val actualUpdatedSegment = segmentTransformSlot.captured(EmptyWheelSegment)

        assertEquals(expectedUpdatedSegment, actualUpdatedSegment)
    }

    @Test
    fun viewModel_deletesThumbnail_onRemoveThumbnailEvent() = runTest {
        every {
            getWheelSegmentStreamUseCase(SEGMENT_ID)
        } returns flow {
            emit(EmptyWheelSegment.copy(thumbnailPath = "path/to/thumbnail.png"))
        }

        coEvery { deleteThumbnailUseCase() } just runs
        coEvery { updateWheelSegmentUseCase(any(), any()) } just runs

        initViewModel()

        viewModel.onEvent(EditSegmentUiEvent.RemoveImage)

        advanceUntilIdle()

        coVerify { deleteThumbnailUseCase() }
    }
}