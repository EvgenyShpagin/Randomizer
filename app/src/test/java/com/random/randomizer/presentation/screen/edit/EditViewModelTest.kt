package com.random.randomizer.presentation.screen.edit

import com.random.randomizer.MainCoroutineRule
import com.random.randomizer.data.repository.FakeWheelSegmentRepository
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.usecase.CreateWheelSegmentUseCase
import com.random.randomizer.domain.usecase.DeleteWheelSegmentUseCase
import com.random.randomizer.domain.usecase.GetWheelSegmentsStreamUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test


private val EmptyWheelSegment = WheelSegment(1, "", "", null, null)

@OptIn(ExperimentalCoroutinesApi::class)
class EditViewModelTest {

    private lateinit var viewModel: EditViewModel

    private lateinit var wheelSegmentRepository: FakeWheelSegmentRepository

    val mappers = FakeEditMappers

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        wheelSegmentRepository = FakeWheelSegmentRepository()
        viewModel = EditViewModel(
            GetWheelSegmentsStreamUseCase(wheelSegmentRepository),
            CreateWheelSegmentUseCase(wheelSegmentRepository),
            DeleteWheelSegmentUseCase(
                wheelSegmentRepository
            ),
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
}