package com.random.randomizer.domain.usecase

import com.random.randomizer.data.FakeWheelSegmentRepository
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.repository.WheelSegmentRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class FixSavedWheelSegmentUseCaseTest {

    // Subject under test
    private lateinit var fixSavedWheelSegmentUseCase: FixSavedWheelSegmentUseCase
    private lateinit var wheelSegmentRepository: WheelSegmentRepository

    @Before
    fun setup() {
        wheelSegmentRepository = FakeWheelSegmentRepository()
        fixSavedWheelSegmentUseCase = FixSavedWheelSegmentUseCase(wheelSegmentRepository)
    }

    @Test
    fun trimsAndRemovesExtraWhitespaces_whenWhitespacesArePresent() = runTest {
        // Given - wheel segment with extra whitespaces
        val originalSegment = WheelSegment(
            id = 1,
            title = "  Title    with   extra spaces   ",
            description = "  Description    with   extra spaces   ",
            thumbnail = null,
            customColor = null
        )
        wheelSegmentRepository.add(originalSegment)

        // When - the use case is invoked
        fixSavedWheelSegmentUseCase.invoke(originalSegment.id)

        // Then - the title and description are normalized
        val updatedSegment = wheelSegmentRepository.get(originalSegment.id)!!
        assertEquals("Title with extra spaces", updatedSegment.title)
        assertEquals("Description with extra spaces", updatedSegment.description)
    }

    @Test
    fun leavesValid_whenWhitespacesAreNotPresent() = runTest {
        // Given - wheel segment with no extra whitespaces
        val originalSegment = WheelSegment(
            id = 2,
            title = "Valid Title",
            description = "Valid Description",
            thumbnail = null,
            customColor = null
        )
        wheelSegmentRepository.add(originalSegment)

        // When - the use case is invoked
        fixSavedWheelSegmentUseCase.invoke(originalSegment.id)

        // Then - the title and description remain unchanged
        val updatedSegment = wheelSegmentRepository.get(originalSegment.id)!!
        assertEquals(originalSegment.title, updatedSegment.title)
        assertEquals(originalSegment.description, updatedSegment.description)
    }

    @Test
    fun throwsException_whenWheelSegmentDoesNotExist() = runTest {
        // Given - non-existing wheel segment ID
        val nonExistingId = 99

        // When/Then - exception is thrown
        assertThrows(NullPointerException::class.java) {
            runBlocking {
                fixSavedWheelSegmentUseCase.invoke(nonExistingId)
            }
        }
    }
}