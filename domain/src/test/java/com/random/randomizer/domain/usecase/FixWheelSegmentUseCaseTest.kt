package com.random.randomizer.domain.usecase

import com.random.randomizer.domain.model.WheelSegment
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test

class FixWheelSegmentUseCaseTest {

    // Subject under test
    private val fixWheelSegmentUseCase = FixWheelSegmentUseCase()

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

        // When - the use case is invoked
        val updatedSegment = fixWheelSegmentUseCase.invoke(originalSegment)

        // Then - the title and description are normalized
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

        // When - the use case is invoked
        val updatedSegment = fixWheelSegmentUseCase.invoke(originalSegment)

        // Then - the title and description remain unchanged
        assertEquals(originalSegment.title, updatedSegment.title)
        assertEquals(originalSegment.description, updatedSegment.description)
    }
}