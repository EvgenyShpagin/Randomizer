package com.random.randomizer.data.source

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.filters.SmallTest
import com.random.randomizer.domain.model.Image
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test

@SmallTest
class WheelSegmentDaoTest {

    // using an in-memory database to store data while process is running
    private lateinit var database: WheelSegmentDatabase

    @Before
    fun initDatabase() {
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            WheelSegmentDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @Test
    fun insertAndGetById() = runTest {
        // Given - insert a wheel segment
        val wheelSegment = WheelSegment(
            id = 1,
            title = "Title 1",
            description = "Description 1",
            thumbnail = "thumbnail.png".asImage(),
            customColor = 0x112233
        )
        database.wheelSegmentDao.insert(wheelSegment)

        // When - get the wheel segment by id
        val loadedWheelSegment = database.wheelSegmentDao.getById(wheelSegment.id)

        // Then - the loaded wheel segment is equal to the expected
        assertNotNull(loadedWheelSegment)
        assertEquals(wheelSegment, loadedWheelSegment)
    }

    @Test
    fun getAll_returnsWheelSegments_whenExist() = runTest {
        // Given - insert a wheel segment
        val wheelSegment = WheelSegment(
            id = 1,
            title = "Title 1",
            description = "Description 1",
            thumbnail = "thumbnail.png".asImage(),
            customColor = 0x112233
        )
        database.wheelSegmentDao.insert(wheelSegment)

        // When - get all wheel segment
        val loadedWheelSegments = database.wheelSegmentDao.getAll()

        // Then - the loaded wheel segment is equal to the expected
        assertEquals(1, loadedWheelSegments.count())
        assertEquals(wheelSegment, loadedWheelSegments.single())
    }

    @Test
    fun update_replacesWheelSegment_whenExists() = runTest {
        // Given - insert a wheel segment
        val wheelSegment = WheelSegment(
            id = 1,
            title = "Title 1",
            description = "Description 1",
            thumbnail = "thumbnail.png".asImage(),
            customColor = 0x112233
        )
        database.wheelSegmentDao.insert(wheelSegment)

        // When - update the wheel segment
        val newWheelSegment = WheelSegment(
            id = 1,
            title = "Title 2",
            description = "Description 2",
            thumbnail = "thumbnail2.png".asImage(),
            customColor = null
        )
        database.wheelSegmentDao.update(newWheelSegment)

        // Then - the loaded wheel segment is equal to the new
        val loadedWheelSegment = database.wheelSegmentDao.getById(wheelSegment.id)
        assertNotNull(loadedWheelSegment)
        assertEquals(newWheelSegment, loadedWheelSegment)
    }


    @Test
    fun deleteById_deletesWheelSegment_whenExists() = runTest {
        // Given - insert a wheel segment
        val wheelSegment = WheelSegment(
            id = 1,
            title = "Title 1",
            description = "Description 1",
            thumbnail = "thumbnail.png".asImage(),
            customColor = 0x112233
        )
        database.wheelSegmentDao.insert(wheelSegment)

        // When - deleting the wheel segment by id
        database.wheelSegmentDao.deleteById(wheelSegment.id)

        // Then - the list is empty
        val tasks = database.wheelSegmentDao.getAll()
        assertEquals(true, tasks.isEmpty())
    }

    @Test
    fun upsert_returnsNewId_whenWheelSegmentIdWas0() = runTest {
        // Given - insert a wheel segment with id = 0
        val wheelSegment = WheelSegment(
            id = 0,
            title = "Title 1",
            description = "Description 1",
            thumbnail = "thumbnail.png".asImage(),
            customColor = 0x112233
        )

        // When - save wheel segment
        val id = database.wheelSegmentDao.insert(wheelSegment)

        // Then - wheel segment's id was updated
        assertNotEquals(wheelSegment.id, id)
    }

    @Test
    fun observeById_returnsWheelSegment_whenExists() = runTest {
        // Given - insert a wheel segment
        val wheelSegment = WheelSegment(
            id = 1,
            title = "Title 1",
            description = "Description 1",
            thumbnail = "thumbnail.png".asImage(),
            customColor = 0x112233
        )
        database.wheelSegmentDao.insert(wheelSegment)

        // When - observe given wheel segment
        val observed = database.wheelSegmentDao.observeById(wheelSegment.id).first()

        // Then - verify got inserted wheel segment
        assertEquals(wheelSegment, observed)
    }

    @Test
    fun observeAll_returnsWheelSegment_whenExists() = runTest {
        // Given - insert a wheel segment
        val wheelSegment = WheelSegment(
            id = 1,
            title = "Title 1",
            description = "Description 1",
            thumbnail = "thumbnail.png".asImage(),
            customColor = 0x112233
        )
        database.wheelSegmentDao.insert(wheelSegment)

        // When - observe given wheel segment
        val firstObservedList = database.wheelSegmentDao.observeAll().first()

        // Then - verify got inserted wheel segment
        assertEquals(1, firstObservedList.count())
        assertEquals(wheelSegment, firstObservedList.single())
    }

    private fun String.asImage(): Image {
        return Image(id = this, byteArrayOf())
    }
}