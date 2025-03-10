package com.random.randomizer.presentation.screen.results

import androidx.lifecycle.SavedStateHandle
import com.random.randomizer.MainCoroutineRule
import com.random.randomizer.data.repository.FakeWheelSegmentRepository
import com.random.randomizer.data.repository.addMultiple
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.usecase.DeleteWheelSegmentUseCase
import com.random.randomizer.domain.usecase.GetWheelSegmentStreamUseCase
import com.random.randomizer.domain.usecase.GetWheelSegmentsUseCase
import com.random.randomizer.presentation.navigation.Destination
import com.random.randomizer.presentation.test_util.mockkToRoute
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ResultsViewModelTest {

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var repository: FakeWheelSegmentRepository

    // Subject under testing
    private lateinit var viewModel: ResultsViewModel

    private val winner = segmentList.first()

    @Before
    fun setup() {
        repository = FakeWheelSegmentRepository()

        val savedStateHandle = SavedStateHandle().apply {
            mockkToRoute(Destination.Results(winner.id))
        }

        viewModel = ResultsViewModel(
            savedStateHandle,
            GetWheelSegmentStreamUseCase(repository),
            GetWheelSegmentsUseCase(repository),
            DeleteWheelSegmentUseCase(repository)
        )
    }

    @Test
    fun triggersSpinEffect_onSpinEvent() = runTest {
        // Given - multiple segments
        repository.addMultiple(segmentList)

        // When - on Spin event
        viewModel.onEvent(ResultsUiEvent.Spin)

        // Then - verify effect emitted
        val effect = viewModel.uiEffect.firstOrNull()

        assertTrue(effect != null)
    }

    @Test
    fun deletesSegment_onDeleteAndSpinEvent() = runTest {
        // Given - multiple segments
        repository.addMultiple(segmentList)

        // When - on SpinAndDelete event
        viewModel.onEvent(ResultsUiEvent.SpinAndDelete)

        // Then - verify winner has been deleted
        val expectedList = segmentList - winner
        val actualList = repository.getAll()
        assertEquals(expectedList, actualList)
    }

    @Test
    fun triggersSpinEffect_onDeleteAndSpinEvent() = runTest {
        // Given - multiple segments
        repository.addMultiple(segmentList)

        // When - on SpinAndDelete event
        viewModel.onEvent(ResultsUiEvent.SpinAndDelete)

        // Then - verify effect emitted
        val effect = viewModel.uiEffect.firstOrNull()

        assertTrue(effect != null)
    }

    private companion object {
        val segmentList = List(10) { i ->
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