package com.random.randomizer.domain.usecase

import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.model.contentEquals
import com.random.randomizer.domain.repository.WheelSegmentRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Test

class CreateWheelSegmentUseCaseTest {

    val repository = mockk<WheelSegmentRepository>()
    val createWheelSegmentUseCase = CreateWheelSegmentUseCase(repository)

    @Test
    fun addsSegment() = runTest {
        coEvery { repository.add(any()) } returns 1

        createWheelSegmentUseCase()

        coVerify { repository.add(any()) }
    }

    @Test
    fun returnsEmptySegment() = runTest {
        coEvery { repository.add(any()) } returns 1

        val actual = createWheelSegmentUseCase()
        val expected = WheelSegment(0, "", "", null, null)

        assertTrue(actual.contentEquals(expected))
    }

    @Test
    fun replacesId() = runTest {
        coEvery { repository.add(any()) } returns 1

        val createdSegment = createWheelSegmentUseCase()

        assertEquals(createdSegment.id, 1)
    }
}