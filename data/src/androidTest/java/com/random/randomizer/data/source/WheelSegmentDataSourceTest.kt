package com.random.randomizer.data.source

import android.content.Context
import android.graphics.Bitmap
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.random.randomizer.data.util.toByteArray
import com.random.randomizer.domain.model.Image
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import java.io.File

class WheelSegmentDataSourceTest {

    // Subject under test
    lateinit var dataSource: WheelSegmentDataSourceImpl

    // using an in-memory database to store data while process is running
    private lateinit var database: WheelSegmentDatabase

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            WheelSegmentDatabase::class.java
        ).allowMainThreadQueries().build()

        dataSource = WheelSegmentDataSourceImpl(
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

    @Test
    fun upsert_deletesThumbnail_whenRemovedAndUsedBySingleSegment() = runTest {
        // Given - insert a wheel segment with thumbnail
        val wheelSegment = WheelSegment(
            id = 1,
            title = "Title 1",
            description = "Description 1",
            thumbnail = createImage(),
            customColor = 0x112233
        )
        dataSource.upsert(wheelSegment)

        val thumbnailFilename = dataSource.getById(wheelSegment.id)!!.thumbnail!!.id

        // When - update wheel segment by removing the thumbnail
        val updatedWheelSegment = wheelSegment.copy(thumbnail = null)
        dataSource.upsert(updatedWheelSegment)

        val thumbnailsDir = getApplicationContext<Context>().filesDir
        val thumbnailFile = File(thumbnailsDir, thumbnailFilename)

        // Then - verify thumbnail has been deleted
        assertFalse(thumbnailFile.exists())
    }

    @Test
    fun upsert_keepsThumbnail_whenRemovedAndUsedByMultipleSegments() = runTest {
        // Given - insert wheel segments
        val wheelSegments = List(2) { i ->
            WheelSegment(
                id = i + 1,
                title = "Title $i",
                description = "Description $i",
                thumbnail = createImage(),
                customColor = 0x112233
            )
        }
        dataSource.upsertMultiple(wheelSegments)

        val savedWheelSegments = dataSource.getAll()
        val thumbnailFilename = savedWheelSegments.first().thumbnail!!.id

        // When - update the first wheel segment by removing the thumbnail
        val updatedWheelSegment = savedWheelSegments.first().copy(thumbnail = null)
        dataSource.upsert(updatedWheelSegment)

        val thumbnailsDir = getApplicationContext<Context>().filesDir
        val thumbnailFile = File(thumbnailsDir, thumbnailFilename)

        // Then - verify thumbnail has not been deleted
        assertTrue(thumbnailFile.exists())
    }

    @Test
    fun deleteById_deletesWheelSegment_whenExists() = runTest {
        // Given - insert a wheel segment
        val wheelSegment = WheelSegment(
            id = 1,
            title = "Title 1",
            description = "Description 1",
            thumbnail = createImage(),
            customColor = 0x112233
        )
        dataSource.upsert(wheelSegment)

        // When - deleting the wheel segment by id
        dataSource.deleteById(wheelSegment.id)

        // Then - the list is empty
        val wheelSegments = dataSource.getAll()
        assertEquals(true, wheelSegments.isEmpty())
    }

    @Test // Implementation details test
    fun deleteById_deletesThumbnail_whenUsedByOneSegment() = runTest {
        // Given - insert a wheel segment
        val wheelSegment = WheelSegment(
            id = 1,
            title = "Title 1",
            description = "Description 1",
            thumbnail = createImage(),
            customColor = 0x112233
        )
        dataSource.upsert(wheelSegment)

        val savedWheelSegment = dataSource.getById(wheelSegment.id)

        // When - deleting the wheel segment by id
        dataSource.deleteById(wheelSegment.id)

        val thumbnailFilename = savedWheelSegment!!.thumbnail!!.id
        val thumbnailsDir = getApplicationContext<Context>().filesDir
        val thumbnailFile = File(thumbnailsDir, thumbnailFilename)

        // Then - verify thumbnail has been deleted
        assertFalse(thumbnailFile.exists())
    }

    @Test // Implementation details test
    fun deleteById_doesNotDeleteThumbnail_whenUsedByMultipleSegments() = runTest {
        // Given - insert wheel segments
        val wheelSegments = List(2) { i ->
            WheelSegment(
                id = i + 1,
                title = "Title $i",
                description = "Description $i",
                thumbnail = createImage(),
                customColor = 0x112233
            )
        }
        dataSource.upsertMultiple(wheelSegments)

        val savedWheelSegments = dataSource.getAll()

        // When - deleting the wheel segment by id
        dataSource.deleteById(savedWheelSegments.first().id)

        val thumbnailFilename = savedWheelSegments.first().thumbnail!!.id
        val thumbnailsDir = getApplicationContext<Context>().filesDir
        val thumbnailFile = File(thumbnailsDir, thumbnailFilename)

        // Then - verify thumbnail has not been deleted
        assertTrue(thumbnailFile.exists())
    }

    @Test
    fun observeById_returnsWheelSegment_whenExists() = runTest {
        // Given - insert a wheel segment
        val wheelSegment = WheelSegment(
            id = 1,
            title = "Title 1",
            description = "Description 1",
            thumbnail = createImage(),
            customColor = 0x112233
        )
        dataSource.upsert(wheelSegment)

        // When - observe given wheel segment
        val observed = dataSource.observeById(wheelSegment.id).first()

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
            thumbnail = createImage(),
            customColor = 0x112233
        )
        dataSource.upsert(wheelSegment)

        // When - observe given wheel segment
        val firstObservedList = dataSource.observeAll().first()

        // Then - verify got inserted wheel segment
        assertEquals(1, firstObservedList.count())
        assertEquals(wheelSegment, firstObservedList.single())
    }

    private fun createImage(): Image {
        val bitmap = Bitmap.createBitmap(4, 4, Bitmap.Config.ARGB_8888)
        return Image(id = "image.png", data = bitmap.toByteArray())
    }

    private suspend fun WheelSegmentDataSourceImpl.upsertMultiple(wheelSegments: List<WheelSegment>) {
        wheelSegments.forEach { upsert(it) }
    }
}