package com.random.randomizer.presentation.screen.edit

import com.random.randomizer.MainCoroutineRule
import com.random.randomizer.data.FakeWheelSegmentRepository
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.model.contentEquals
import com.random.randomizer.domain.usecase.CreateWheelSegmentUseCase
import com.random.randomizer.domain.usecase.DeleteWheelSegmentUseCase
import com.random.randomizer.domain.usecase.FixSavedWheelSegmentUseCase
import com.random.randomizer.domain.usecase.GetWheelSegmentsStreamUseCase
import com.random.randomizer.domain.usecase.MakeWheelSegmentUniqueUseCase
import com.random.randomizer.domain.usecase.ValidateWheelSegmentUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test


private val EmptyWheelSegment = WheelSegment(1, "", "", null, null)
private val NonEmptyWheelSegment = WheelSegment(1, "Title", "Description", null, null)

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
            FixSavedWheelSegmentUseCase(wheelSegmentRepository),
            DeleteWheelSegmentUseCase(
                wheelSegmentRepository
            ),
            ValidateWheelSegmentUseCase(wheelSegmentRepository),
            MakeWheelSegmentUniqueUseCase(wheelSegmentRepository),
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

    @Test
    fun removesExtraWhitespaces_onFinishEdit() = runTest {
        // Given - one wheel segment with extra whitespaces
        wheelSegmentRepository.add(
            WheelSegment(
                1, " Title with   some whitespaces  ",
                "  and   inside description", null, null
            )
        )

        val uiStateSegments = viewModel.uiState.value.wheelSegments

        // When - on start edit segment with extra whitespaces
        viewModel.onEvent(
            EditUiEvent.EditSegment(uiStateSegments.single())
        ) // and finish it
        viewModel.onEvent(EditUiEvent.FinishSegmentEdit)

        val expectedTitle = "Title with some whitespaces"
        val actualTitle = wheelSegmentRepository.get(1)!!.title

        val expectedDescription = "and inside description"
        val actualDescription = wheelSegmentRepository.get(1)!!.description

        // Then - verify wheel segment's extra whitespaces were removed
        assertEquals(expectedTitle, actualTitle)
        assertEquals(expectedDescription, actualDescription)
    }

    @Test
    fun makesUnique_onFinishEdit() = runTest {
        // Given - one wheel segment
        wheelSegmentRepository.add(NonEmptyWheelSegment)

        // When - on create new segment
        viewModel.onEvent(EditUiEvent.CreateSegment)
        // after creating a copy of already existing wheel segment
        val createdWheelSegmentId = viewModel.uiState.value.currentlyEditedSegmentId!!
        val duplicate = NonEmptyWheelSegment.copy(id = createdWheelSegmentId)
        // update wheel segment being edited to duplicate
        wheelSegmentRepository.update(duplicate)
        // and finish edit
        viewModel.onEvent(EditUiEvent.FinishSegmentEdit)

        val savedWheelSegments = wheelSegmentRepository.getAll()

        // Then - verify 2 wheel segments are saved
        assertEquals(2, savedWheelSegments.count())
        // and they are different
        assertFalse(savedWheelSegments[0].contentEquals(savedWheelSegments[1]))
    }
}