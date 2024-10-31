package com.random.randomizer.presentation.screen.edit

import com.random.randomizer.domain.common.Result
import com.random.randomizer.domain.error.WheelSegmentValidationError
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.usecase.CreateWheelSegmentUseCase
import com.random.randomizer.domain.usecase.DeleteWheelSegmentUseCase
import com.random.randomizer.domain.usecase.GetWheelSegmentsStreamUseCase
import com.random.randomizer.domain.usecase.MakeWheelSegmentUniqueUseCase
import com.random.randomizer.domain.usecase.ValidateWheelSegmentUseCase
import com.random.randomizer.presentation.core.toUiState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


private val EmptyWheelSegment = WheelSegment(1, "", "", null, null)

@OptIn(ExperimentalCoroutinesApi::class)
class EditViewModelTest {

    lateinit var viewModel: EditViewModel

    val testDispatcher = StandardTestDispatcher()

    val getWheelSegmentsStreamUseCase = mockk<GetWheelSegmentsStreamUseCase>()
    val createWheelSegmentUseCase = mockk<CreateWheelSegmentUseCase>()
    val deleteWheelSegmentUseCase = mockk<DeleteWheelSegmentUseCase>()
    val validateWheelSegmentUseCase = mockk<ValidateWheelSegmentUseCase>()
    val makeWheelSegmentUniqueUseCase = mockk<MakeWheelSegmentUniqueUseCase>()


    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun editViewModel_updatesState_whenCreated() = runTest {
        val wheelSegments = listOf(EmptyWheelSegment)

        every { getWheelSegmentsStreamUseCase() } returns flow { emit(wheelSegments) }

        initViewModel()

        advanceUntilIdle()

        val expected = wheelSegments.map { it.toUiState() }
        val actual = viewModel.uiState.value.wheelSegments

        assertEquals(expected, actual)
    }

    @Test
    fun editViewModel_createsSegment_onAddEvent() = runTest {
        every { getWheelSegmentsStreamUseCase() } returns flow {}
        coEvery { createWheelSegmentUseCase() } returns EmptyWheelSegment

        initViewModel()

        viewModel.onEvent(EditUiEvent.CreateSegment)

        advanceUntilIdle()

        coVerify { createWheelSegmentUseCase() }
    }

    @Test
    fun editViewModel_updatesEditedSegment_onEditEvent() = runTest {
        val wheelSegments = listOf(EmptyWheelSegment)

        every { getWheelSegmentsStreamUseCase() } returns flow { emit(wheelSegments) }

        initViewModel()

        viewModel.onEvent(EditUiEvent.EditSegment(EmptyWheelSegment.toUiState()))

        advanceUntilIdle()

        val expected = EmptyWheelSegment.id
        val actual = viewModel.uiState.value.currentlyEditedSegmentId

        assertEquals(expected, actual)
    }

    @Test
    fun editViewModel_deletesEmptyEditedSegment_onFinishEdit() = runTest {
        val wheelSegments = listOf(EmptyWheelSegment)

        every { getWheelSegmentsStreamUseCase() } returns flow { emit(wheelSegments) }

        initViewModel()

        viewModel.onEvent(EditUiEvent.EditSegment(EmptyWheelSegment.toUiState()))

        advanceUntilIdle()

        coEvery {
            validateWheelSegmentUseCase(EmptyWheelSegment)
        } returns Result.Failure(WheelSegmentValidationError.Empty)

        coEvery { deleteWheelSegmentUseCase(EmptyWheelSegment.id) } just runs

        viewModel.onEvent(EditUiEvent.FinishSegmentEdit)

        advanceUntilIdle()

        coVerify { deleteWheelSegmentUseCase(EmptyWheelSegment.id) }
    }

    private fun initViewModel() {
        viewModel = EditViewModel(
            getWheelSegmentsStreamUseCase = getWheelSegmentsStreamUseCase,
            createWheelSegmentUseCase = createWheelSegmentUseCase,
            deleteWheelSegmentUseCase = deleteWheelSegmentUseCase,
            validateWheelSegmentUseCase = validateWheelSegmentUseCase,
            makeWheelSegmentUniqueUseCase = makeWheelSegmentUniqueUseCase
        )
    }
}