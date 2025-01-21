package com.random.randomizer.data.repository

import com.random.randomizer.data.source.FakeWheelSegmentDataSource
import com.random.randomizer.data.source.WheelSegmentDataSource
import com.random.randomizer.data.util.toData
import com.random.randomizer.data.util.toDomain
import com.random.randomizer.domain.model.WheelSegment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WheelSegmentRepositoryTest {

    // Subject under test
    private lateinit var repository: WheelSegmentRepositoryImpl

    private lateinit var dataSource: WheelSegmentDataSource

    private var testDispatcher = UnconfinedTestDispatcher()
    private var testScope = TestScope(testDispatcher)

    @Before
    fun setUp() {
        dataSource = FakeWheelSegmentDataSource()
        repository = WheelSegmentRepositoryImpl(
            dataSource = dataSource,
            ioDispatcher = testDispatcher,
            externalScope = testScope
        )
    }

    @Test
    fun add_savesWheelSegment() = testScope.runTest {
        // Given - a segment to insert
        val segment = WheelSegment(1, "Title", "Description", null, null)

        // When - insert
        repository.add(segment)

        // Then - verify it has been saved
        assertEquals(
            segment,
            dataSource.getById(segment.id)?.toDomain()
        )
    }

    @Test
    fun add_returnsNewId_whenPassedExistingId() = testScope.runTest {
        // Given - an inserted segment
        val existingSegment = WheelSegment(1, "Title", "Description", null, null)
        repository.add(existingSegment)

        // When - inserted with existing id
        val newSegment = WheelSegment(1, "Title 2", "Description 2", null, null)
        val newSegmentId = repository.add(newSegment)

        // Then - verify returned id has changed
        assertNotEquals(existingSegment.id, newSegmentId)
    }

    @Test
    fun getAll_returnsAllSegments_whenExist() = testScope.runTest {
        // Given - multiple saved segments
        repeat(5) { i ->
            val wheelSegment = WheelSegment(i, "Title $i", "", null, null).toData()
            dataSource.insert(wheelSegment)
        }

        // When - getAll called
        val wheelSegments = repository.getAll()

        // Then - verify the segments are the same
        assertEquals(
            dataSource.getAll().toDomain(),
            wheelSegments
        )
    }

}