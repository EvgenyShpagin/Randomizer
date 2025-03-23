package com.random.randomizer.presentation.screen.home

import com.random.randomizer.MainCoroutineRule
import com.random.randomizer.data.repository.FakeWheelSegmentRepository
import com.random.randomizer.data.repository.addMultiple
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.usecase.DeleteWheelSegmentUseCase
import com.random.randomizer.domain.usecase.GetWheelSegmentsStreamUseCase
import com.random.randomizer.presentation.core.toPresentation
import com.random.randomizer.presentation.screen.home.HomeUiEvent.DeleteSegment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {

    private lateinit var repository: FakeWheelSegmentRepository

    // Subject under testing
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        repository = FakeWheelSegmentRepository()
        viewModel = HomeViewModel(
            GetWheelSegmentsStreamUseCase(repository),
            DeleteWheelSegmentUseCase(repository)
        )
    }

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Test
    fun updatesState_whenCreated() = runTest {
        // Given - multiple segments
        repository.addMultiple(SegmentList)

        // When - on startup

        val expected = HomeUiState(
            wheelSegments = SegmentList.map { toPresentation(it) },
            isLoading = false
        )
        val actual = viewModel.uiState.first()

        // Then - verify state is "blank"
        assertEquals(expected, actual)
    }

    @Test
    fun deletesSegment_onDeleteEvent() = runTest {
        // Given - multiple segments
        repository.addMultiple(SegmentList)

        val firstDomainSegment = SegmentList.first()
        val firstPresentationSegment = toPresentation(SegmentList.first())

        // When - delete event invoked
        viewModel.onEvent(DeleteSegment(firstPresentationSegment))

        // Then - verify UI state and repository updated
        val expectedDomainList = SegmentList - firstDomainSegment
        val actualDomainList = repository.getAll()
        assertEquals(expectedDomainList, actualDomainList)

        val expectedPresentationList = expectedDomainList.map { toPresentation(it) }
        val actualPresentationList = viewModel.uiState.first().wheelSegments
        assertEquals(expectedPresentationList, actualPresentationList)
    }

    private companion object {
        val SegmentList = List(10) { i ->
            WheelSegment(
                id = i + 1, // start from 1 (0 is not valid id)
                title = "Segment $i",
                description = "",
                thumbnail = null,
                customColor = null
            )
        }
    }
}