package com.random.randomizer.presentation.screen.spin

import com.random.randomizer.MainCoroutineRule
import com.random.randomizer.data.repository.FakeWheelSegmentRepository
import com.random.randomizer.data.repository.addMultiple
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.repository.WheelSegmentRepository
import com.random.randomizer.domain.usecase.GetWheelSegmentsUseCase
import com.random.randomizer.domain.usecase.SelectRandomWheelSegmentUseCase
import com.random.randomizer.presentation.core.CoreMappersImpl
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SpinViewModelTest {

    // Subject under test
    private lateinit var viewModel: SpinViewModel

    // Use a fake repository to be injected into the viewmodel
    private lateinit var repository: WheelSegmentRepository

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        repository = FakeWheelSegmentRepository()
        viewModel = SpinViewModel(
            getWheelSegmentsUseCase = GetWheelSegmentsUseCase(repository),
            selectRandomWheelSegmentUseCase = SelectRandomWheelSegmentUseCase(repository),
            mappers = SpinMappersImpl(CoreMappersImpl())
        )
    }

    @Test
    fun extendsListToAtLeast100_whenSegmentCountLessThan100() = runTest {
        // Given - multiple segments
        repository.addMultiple(SegmentList)

        // When - on first UI state update
        viewModel.uiState.first()

        // Then - verify segment count is at least 100
        assertTrue(viewModel.uiState.value.wheelSegments.count() >= 100)
    }

    @Test
    fun duplicatesEachSegmentMultipleTimes_beforeSpin() = runTest {
        // Given - multiple segments
        repository.addMultiple(SegmentList)

        // When - on first UI state update
        val uiStateSegments = viewModel.uiState.first().wheelSegments

        val firstItemCount = uiStateSegments.count { it.id == SegmentList.first().id }
        val lastItemCount = uiStateSegments.count { it.id == SegmentList.last().id }

        // Then - verify each item is present 3 times or more
        assertTrue(firstItemCount >= 3)
        assertTrue(lastItemCount >= 3)
    }

    @Test
    fun selectsWinnerCloselyToCenter() = runTest {
        // Given - multiple segments
        repository.addMultiple(SegmentList)

        // When - on UI state when targetIndex is set
        val uiState = viewModel.uiState.first { state -> state.targetIndex != -1 }

        val uiSegmentCount = uiState.wheelSegments.count()

        val acceptableTargetIndexRange =
            (uiSegmentCount * 0.25).toInt() until (uiSegmentCount * 0.75).toInt()

        // Then - verify winner index is within center half of segments
        assertTrue(uiState.targetIndex in acceptableTargetIndexRange)
    }

    private companion object {
        val SegmentList = List(10) { i ->
            WheelSegment(i, "Segment $i", "", null, null)
        }
    }
}