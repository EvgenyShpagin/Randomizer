package com.random.randomizer.data.source

import android.graphics.Bitmap
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.random.randomizer.data.util.toByteArray
import com.random.randomizer.domain.model.Image
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test

class WheelSegmentDataSourceTest {

    // Subject under test
    lateinit var dataSource: WheelSegmentDataSource

    // using an in-memory database to store data while process is running
    private lateinit var database: WheelSegmentDatabase

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            WheelSegmentDatabase::class.java
        ).allowMainThreadQueries().build()

        dataSource = WheelSegmentDataSource(
            context = getApplicationContext(),
            wheelSegmentDao = database.wheelSegmentDao
        )
    }

    @Test
    fun upsertAndGetById() = runTest {
        // Given - wheel segment in database
        val wheelSegment = WheelSegment(
            id = 1,
            title = "Title 1",
            description = "Description 1",
            thumbnail = createImage(),
            customColor = 0x112233
        )
        dataSource.upsert(wheelSegment)

        // When - get the wheel segment by id
        val loadedWheelSegment = dataSource.getById(wheelSegment.id)

        // Then - the loaded wheel segment is equal to the expected
        assertEquals(wheelSegment, loadedWheelSegment)
    }

    @Test
    fun getAll_returnsWheelSegments_whenExist() = runTest {
        // Given - insert a wheel segment
        val wheelSegment = WheelSegment(
            id = 1,
            title = "Title 1",
            description = "Description 1",
            thumbnail = createImage(),
            customColor = 0x112233
        )
        dataSource.upsert(wheelSegment)

        // When - get all wheel segment
        val loadedWheelSegments = dataSource.getAll()

        // Then - the loaded wheel segment is equal to the expected
        assertEquals(1, loadedWheelSegments.count())
        assertEquals(wheelSegment, loadedWheelSegments.single())
    }

    @Test
    fun upsert_replacesWheelSegment_onConflict() = runTest {
        // Given - insert a wheel segment
        val wheelSegment = WheelSegment(
            id = 1,
            title = "Title 1",
            description = "Description 1",
            thumbnail = createImage(),
            customColor = 0x112233
        )
        dataSource.upsert(wheelSegment)

        // When - a wheel segment with the same id is inserted
        val newWheelSegment = WheelSegment(
            id = 1,
            title = "Title 2",
            description = "Description 2",
            thumbnail = createImage(),
            customColor = null
        )
        dataSource.upsert(newWheelSegment)

        // Then - the loaded wheel segment is equal to the new
        val loadedWheelSegment = dataSource.getById(wheelSegment.id)
        assertNotNull(loadedWheelSegment)
        assertEquals(newWheelSegment, loadedWheelSegment)
    }

    @Test
    fun upsert_returnsNewId_whenWheelSegmentIdWas0() = runTest {
        // Given - insert a wheel segment with id = 0
        val wheelSegment = WheelSegment(
            id = 0,
            title = "Title 1",
            description = "Description 1",
            thumbnail = createImage(),
            customColor = 0x112233
        )

        // When - save wheel segment
        val id = database.wheelSegmentDao.upsert(wheelSegment)

        // Then - wheel segment's id was updated
        assertNotEquals(wheelSegment.id, id)
    }

    private fun createImage(): Image {
        val bitmap = Bitmap.createBitmap(4, 4, Bitmap.Config.ARGB_8888)
        return Image(id = "image.png", data = bitmap.toByteArray())
    }
}