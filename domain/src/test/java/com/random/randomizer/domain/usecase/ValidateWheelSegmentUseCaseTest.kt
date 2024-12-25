package com.random.randomizer.domain.usecase

import com.random.randomizer.domain.common.Result
import com.random.randomizer.domain.error.WheelSegmentValidationError
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.repository.WheelSegmentRepository
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ValidateWheelSegmentUseCaseTest {

    private val repository = mockk<WheelSegmentRepository>()
    // Subject under test
    private val validateWheelSegmentUseCase = ValidateWheelSegmentUseCase(repository)

    @Test
    fun returnsFailure_whenSegmentIsEmpty() = runTest {
        // Given - empty list
        coEvery { repository.getAll() } returns emptyList()

        val emptySegment = WheelSegment(1, "", "", null, null)

        // When - invoked
        val result = validateWheelSegmentUseCase(emptySegment)

        // Then - verify WheelSegmentValidationError.Empty returned
        assertTrue(result is Result.Failure && result.error == WheelSegmentValidationError.Empty)
    }

    @Test
    fun returnsFailure_whenSegmentCopiesExistingOne() = runTest {
        // Given - one item
        val savedSegment = WheelSegment(1, "Title", "", null, null)
        coEvery { repository.getAll() } returns listOf(savedSegment)

        val notSavedSegment = savedSegment.copy(id = 2)

        // When - invoked
        val result = validateWheelSegmentUseCase(notSavedSegment)

        // Then - verify WheelSegmentValidationError.AlreadyExists returned
        assertTrue(
            result is Result.Failure && result.error == WheelSegmentValidationError.AlreadyExists
        )
    }

    @Test
    fun returnsSuccess_otherwise() = runTest {
        // Given - empty list
        coEvery { repository.getAll() } returns emptyList()

        val correctSegment = WheelSegment(1, "Title", "", null, null)

        // When - invoked
        val result = validateWheelSegmentUseCase(correctSegment)

        // Then - verify success is returned
        assertTrue(result is Result.Success)
    }
}