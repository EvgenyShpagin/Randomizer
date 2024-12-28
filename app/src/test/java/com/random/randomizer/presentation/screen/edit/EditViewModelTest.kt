package com.random.randomizer.presentation.screen.edit

import com.random.randomizer.MainCoroutineRule
import com.random.randomizer.data.FakeThumbnailRepository
import com.random.randomizer.data.FakeWheelSegmentRepository
import com.random.randomizer.domain.common.Result
import com.random.randomizer.domain.error.WheelSegmentValidationError
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.usecase.CreateWheelSegmentUseCase
import com.random.randomizer.domain.usecase.DeleteThumbnailUseCase
import com.random.randomizer.domain.usecase.DeleteWheelSegmentUseCase
import com.random.randomizer.domain.usecase.GetWheelSegmentsStreamUseCase
import com.random.randomizer.domain.usecase.MakeWheelSegmentUniqueUseCase
import com.random.randomizer.domain.usecase.ValidateWheelSegmentUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
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
    private lateinit var thumbnailRepository: FakeThumbnailRepository

    val getWheelSegmentsStreamUseCase = mockk<GetWheelSegmentsStreamUseCase>()
    val createWheelSegmentUseCase = mockk<CreateWheelSegmentUseCase>()
    val deleteWheelSegmentUseCase = mockk<DeleteWheelSegmentUseCase>()
    val validateWheelSegmentUseCase = mockk<ValidateWheelSegmentUseCase>()
    val makeWheelSegmentUniqueUseCase = mockk<MakeWheelSegmentUniqueUseCase>()

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
                DeleteThumbnailUseCase(thumbnailRepository)
            ),
            ValidateWheelSegmentUseCase(wheelSegmentRepository),
            MakeWheelSegmentUniqueUseCase(),
            mappers
        )
    }

    @Test
    fun editViewModel_updatesState_whenCreated() = runTest {
        val wheelSegments = listOf(EmptyWheelSegment)

        every { getWheelSegmentsStreamUseCase() } returns flow { emit(wheelSegments) }

        initViewModel()

        val expected = wheelSegments.map { mappers.toPresentation(it) }
        val actual = viewModel.uiState.value.wheelSegments

        assertEquals(expected, actual)
    }

    @Test
    fun editViewModel_createsSegment_onAddEvent() = runTest {
        every { getWheelSegmentsStreamUseCase() } returns flow {}
        coEvery { createWheelSegmentUseCase() } returns EmptyWheelSegment

        initViewModel()

        viewModel.onEvent(EditUiEvent.CreateSegment)

        coVerify { createWheelSegmentUseCase() }
    }

    @Test
    fun editViewModel_updatesEditedSegment_onEditEvent() = runTest {
        val wheelSegments = listOf(EmptyWheelSegment)

        every { getWheelSegmentsStreamUseCase() } returns flow { emit(wheelSegments) }

        initViewModel()

        viewModel.onEvent(EditUiEvent.EditSegment(mappers.toPresentation(EmptyWheelSegment)))

        val expected = EmptyWheelSegment.id
        val actual = viewModel.uiState.value.currentlyEditedSegmentId

        assertEquals(expected, actual)
    }

    @Test
    fun editViewModel_deletesEmptyEditedSegment_onFinishEdit() = runTest {
        val wheelSegments = listOf(EmptyWheelSegment)

        every { getWheelSegmentsStreamUseCase() } returns flow { emit(wheelSegments) }

        initViewModel()

        viewModel.onEvent(EditUiEvent.EditSegment(mappers.toPresentation(EmptyWheelSegment)))

        coEvery {
            validateWheelSegmentUseCase(EmptyWheelSegment)
        } returns Result.Failure(WheelSegmentValidationError.Empty)

        coEvery { deleteWheelSegmentUseCase(EmptyWheelSegment.id) } just runs

        viewModel.onEvent(EditUiEvent.FinishSegmentEdit)

        coVerify { deleteWheelSegmentUseCase(EmptyWheelSegment.id) }
    }

    private fun initViewModel() {
        viewModel = EditViewModel(
            getWheelSegmentsStreamUseCase = getWheelSegmentsStreamUseCase,
            createWheelSegmentUseCase = createWheelSegmentUseCase,
            deleteWheelSegmentUseCase = deleteWheelSegmentUseCase,
            validateWheelSegmentUseCase = validateWheelSegmentUseCase,
            makeWheelSegmentUniqueUseCase = makeWheelSegmentUniqueUseCase,
            mappers = mappers
        )
    }
}