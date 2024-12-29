package com.random.randomizer.presentation.screen.edit

import com.random.randomizer.MainCoroutineRule
import com.random.randomizer.data.FakeThumbnailRepository
import com.random.randomizer.data.FakeWheelSegmentRepository
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.usecase.CreateWheelSegmentUseCase
import com.random.randomizer.domain.usecase.DeleteThumbnailUseCase
import com.random.randomizer.domain.usecase.DeleteWheelSegmentUseCase
import com.random.randomizer.domain.usecase.GetWheelSegmentsStreamUseCase
import com.random.randomizer.domain.usecase.MakeWheelSegmentUniqueUseCase
import com.random.randomizer.domain.usecase.ValidateWheelSegmentUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test


private val EmptyWheelSegment = WheelSegment(1, "", "", null, null)

@OptIn(ExperimentalCoroutinesApi::class)
class EditViewModelTest {

    private lateinit var viewModel: EditViewModel

    private lateinit var wheelSegmentRepository: FakeWheelSegmentRepository
    private lateinit var thumbnailRepository: FakeThumbnailRepository

    val mappers = FakeEditMappers

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        wheelSegmentRepository = FakeWheelSegmentRepository()
        thumbnailRepository = FakeThumbnailRepository()
        viewModel = EditViewModel(
            GetWheelSegmentsStreamUseCase(wheelSegmentRepository),
            CreateWheelSegmentUseCase(wheelSegmentRepository),
            DeleteWheelSegmentUseCase(
                wheelSegmentRepository,
                DeleteThumbnailUseCase(thumbnailRepository)
            ),
            ValidateWheelSegmentUseCase(wheelSegmentRepository),
            MakeWheelSegmentUniqueUseCase(),
            mappers
        )
    }

    @Test
    fun updatesState_whenCreated() = runTest {
        // Given - blank wheel segment
        wheelSegmentRepository.add(EmptyWheelSegment)

        // When - on startup

        val expectedSegments = listOf(mappers.toPresentation(EmptyWheelSegment))
        val actualSegments = viewModel.uiState.value.wheelSegments

        // Then - verify UI state is the same as expected
        assertEquals(expectedSegments, actualSegments)
    }

    @Test
    fun createsSegment_onAddEvent() = runTest {
        // Given - empty repository

        // When - on create segment event
        viewModel.onEvent(EditUiEvent.CreateSegment)

        // Then - verify wheel segment was saved
        assertEquals(
            wheelSegmentRepository.getAll().count(),
            1
        )
    }

    @Test
    fun startsEditSegment_onEditEvent() = runTest {
        // Given - blank wheel segment
        wheelSegmentRepository.add(EmptyWheelSegment)

        val uiStateSegments = viewModel.uiState.value.wheelSegments

        // When - on edit segment event
        viewModel.onEvent(
            EditUiEvent.EditSegment(uiStateSegments.single())
        )

        val expected = mappers.toPresentation(EmptyWheelSegment)
        val actual = viewModel.uiState.value.currentlyEditedSegment

        // Then - verify segment is being edited
        assertEquals(expected, actual)
    }

    @Test
    fun deletesEmptyEditedSegment_onFinishEdit() = runTest {
        // Given - no wheel segments

        // When - on create new segment
        viewModel.onEvent(EditUiEvent.CreateSegment)
        // and leave it blank
        viewModel.onEvent(EditUiEvent.FinishSegmentEdit)

        // Then - verify just created blank wheel segment was deleted
        assertTrue(wheelSegmentRepository.getAll().isEmpty())
    }
}