package com.random.randomizer.domain.usecase

import com.random.randomizer.data.repository.FakeWheelSegmentRepository
import com.random.randomizer.data.repository.addMultiple
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.repository.WheelSegmentRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class MakeWheelSegmentUniqueUseCaseTest {

    private lateinit var repository: WheelSegmentRepository

    // Subject under test
    private lateinit var makeWheelSegmentUniqueUseCase: MakeWheelSegmentUniqueUseCase

    @Before
    fun setup() {
        repository = FakeWheelSegmentRepository()
        makeWheelSegmentUniqueUseCase = MakeWheelSegmentUniqueUseCase(repository)
    }

    @Test
    fun keepsOriginal_whenNoCopies() = runTest {
        // Given - 2 different wheel segments
        val wheelSegments = listOf(
            WheelSegment(1, "Title", "Description", null, null),
            WheelSegment(2, "Title 2", "Description 2", null, null)
        )
        repository.addMultiple(wheelSegments)

        // When - invoked
        makeWheelSegmentUniqueUseCase(2)

        // Then - verify second wheel segment was not changed
        val expectedSegment = wheelSegments.last()
        val actualSegment = repository.get(segmentId = 2)
        assertEquals(expectedSegment, actualSegment)
    }

    @Test
    fun appendsCopyIndexInTitle_whenSameExists() = runTest {
        // Given - 2 equal wheel segments
        val wheelSegments = listOf(
            WheelSegment(1, "Title", "Description", null, null),
            WheelSegment(2, "Title", "Description", null, null)
        )
        repository.addMultiple(wheelSegments)

        // When - invoked
        makeWheelSegmentUniqueUseCase(2)

        // Then - verify second wheel segment's title changed
        val expectedTitle = "Title (1)"
        val actualTitle = repository.get(segmentId = 2)!!.title
        assertEquals(expectedTitle, actualTitle)
    }

    @Test
    fun increaseCopyIndexInTitle_whenSameExistsWithCopyIndex() = runTest {
        // Given - 2 equal wheel segments
        val wheelSegments = listOf(
            WheelSegment(1, "Title", "Description", null, null),
            WheelSegment(2, "Title (1)", "Description", null, null),
            WheelSegment(3, "Title (1)", "Description", null, null),
        )
        repository.addMultiple(wheelSegments)

        // When - invoked
        makeWheelSegmentUniqueUseCase(3)

        // Then - verify last wheel segment's title changed
        val expectedTitle = "Title (2)"
        val actualTitle = repository.get(segmentId = 3)!!.title
        assertEquals(expectedTitle, actualTitle)
    }

    @Test
    fun increaseOnlyIndexInLastParentheses_whenTitleHasMultiple() = runTest {
        // Given - 3 equal wheel segments (with copy of copy)
        val wheelSegments = listOf(
            WheelSegment(1, "Title (1)", "Description", null, null),
            WheelSegment(2, "Title (2) (1)", "Description", null, null),
            WheelSegment(3, "Title (2) (1)", "Description", null, null),
        )
        repository.addMultiple(wheelSegments)

        // When - invoked
        makeWheelSegmentUniqueUseCase(3)

        // Then - verify last wheel segment's title changed
        val expectedTitle = "Title (2) (2)"
        val actualTitle = repository.get(segmentId = 3)!!.title
        assertEquals(expectedTitle, actualTitle)
    }
}