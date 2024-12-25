package com.random.randomizer.domain.usecase

import com.random.randomizer.domain.common.Result
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.repository.WheelSegmentRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Test

class UpdateWheelSegmentUseCaseTest {

    private val repository = mockk<WheelSegmentRepository>()

    // Subject under test
    private val updateWheelSegmentUseCase = UpdateWheelSegmentUseCase(repository)

    @Test
    fun returnsFailure_whenSegmentDoesNotExist() = runTest {
        // Given - empty repository
        coEvery { repository.get(segmentId = 1) } returns null

        // When - invoke update
        val result = updateWheelSegmentUseCase(
            wheelSegmentId = 1,
            transform = { segment -> segment } // update does not matter
        )

        // Then - verify was returned Failure
        assertTrue(result is Result.Failure)
    }

    @Test
    fun updatesSegment_whenSegmentExists() = runTest {
        // Given - one item in repository
        val savedWheelSegment = WheelSegment(1, "Title", "Description", null, null)
        val goingToBeUpdatedWheelSegment = WheelSegment(1, "Title new", "Description", null, null)

        coEvery { repository.get(segmentId = 1) } returns savedWheelSegment
        coEvery { repository.update(goingToBeUpdatedWheelSegment) } just runs

        // When - invoke update
        updateWheelSegmentUseCase(
            wheelSegmentId = 1,
            transform = { goingToBeUpdatedWheelSegment }
        )

        // Then - verify update was called with our segment
        coVerify { repository.update(goingToBeUpdatedWheelSegment) }
    }
}