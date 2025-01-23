package com.random.randomizer.domain.usecase

import com.random.randomizer.domain.common.Result
import com.random.randomizer.domain.model.Image
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.repository.WheelSegmentRepository
import com.random.randomizer.domain.util.ImageScaler
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
    private val imageScaler = mockk<ImageScaler>()

    // Subject under test
    private val updateWheelSegmentUseCase = UpdateWheelSegmentUseCase(repository, imageScaler)

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

    @Test
    fun doesNotScale_whenImageIsTheSame() = runTest {
        // Given - one item in repository
        val thumbnail = Image("id1", byteArrayOf())
        val savedWheelSegment = WheelSegment(1, "Title", "Description", thumbnail, null)
        val goingToBeUpdatedWheelSegment = WheelSegment(1, "Title", "Description", thumbnail, null)

        coEvery { repository.get(segmentId = 1) } returns savedWheelSegment
        coEvery { repository.update(goingToBeUpdatedWheelSegment) } just runs

        // When - invoke update
        updateWheelSegmentUseCase(
            wheelSegmentId = 1,
            transform = { goingToBeUpdatedWheelSegment }
        )
        // Then - verify scale was not called
        coVerify(exactly = 0) { imageScaler.scale(any(), any()) }
    }

    @Test
    fun scale_whenImageWasReplaced() = runTest {
        // Given - one item in repository
        val oldThumbnail = Image("id1", byteArrayOf())
        val newThumbnail = Image("id2", byteArrayOf())
        val savedWheelSegment = WheelSegment(1, "Title", "Description", oldThumbnail, null)
        val goingToBeUpdatedWheelSegment =
            WheelSegment(1, "Title", "Description", newThumbnail, null)

        coEvery { repository.get(segmentId = 1) } returns savedWheelSegment
        coEvery { repository.update(goingToBeUpdatedWheelSegment) } just runs
        coEvery { imageScaler.scale(newThumbnail, any()) } returns newThumbnail

        // When - invoke update
        updateWheelSegmentUseCase(
            wheelSegmentId = 1,
            transform = { goingToBeUpdatedWheelSegment }
        )
        // Then - verify scale was called
        coVerify { imageScaler.scale(any(), any()) }
    }
}