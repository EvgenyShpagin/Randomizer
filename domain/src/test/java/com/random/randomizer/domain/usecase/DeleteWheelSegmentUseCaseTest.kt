package com.random.randomizer.domain.usecase

import com.random.randomizer.domain.model.Image
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.repository.WheelSegmentRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DeleteWheelSegmentUseCaseTest {

    private val wheelSegmentRepository = mockk<WheelSegmentRepository>()

    // Subject under test
    private val deleteWheelSegmentUseCase = DeleteWheelSegmentUseCase(wheelSegmentRepository)

    @Test
    fun deletesWheelSegment() = runTest {
        // Given - wheel segment with thumbnail
        val savedWheelSegment = WheelSegment(
            1, "", "", Image("id1", ByteArray(4)), null
        )
        coEvery { wheelSegmentRepository.get(any()) } returns savedWheelSegment
        coEvery { wheelSegmentRepository.deleteById(any()) } just runs

        // When - delete invoked
        deleteWheelSegmentUseCase(1)

        // Then - verify delete methods were called
        coVerify { wheelSegmentRepository.deleteById(1) }
    }

}