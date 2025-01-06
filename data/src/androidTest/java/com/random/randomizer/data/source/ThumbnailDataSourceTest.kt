package com.random.randomizer.data.source

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream

class ThumbnailDataSourceTest {

    // Subject under test
    private lateinit var thumbnailDataSource: ThumbnailDataSource

    private var thumbnail: File? = null

    @Before
    fun setup() {
        thumbnailDataSource = ThumbnailDataSource(getApplicationContext())
    }

    @After
    fun tearDown() {
        thumbnail?.delete()
    }

    @Test
    fun saveThumbnail_returnsFile_whenCorrectInputStreamGot() {
        // Given - a large bitmap as input
        val originalBitmap = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888)
        val inputStream = originalBitmap.toInputStream()

        // When - saving the thumbnail
        thumbnail = thumbnailDataSource.saveThumbnail("test_id", inputStream)

        // Then - verify the file exists and is minimized
        assertNotNull(thumbnail)
        val thumbnail = thumbnail!!
        val savedBitmap = BitmapFactory.decodeFile(thumbnail.path)
        assertNotNull(savedBitmap)
        assertEquals(300, savedBitmap!!.width)
        assertEquals(300, savedBitmap.height)
    }

    @Test
    fun saveThumbnail_returnsNull_whenInvalidInputStreamGot() = runTest {
        // Given - invalid input stream
        val invalidInputStream = ByteArrayInputStream(ByteArray(0))

        // When - saving the thumbnail
        thumbnail = thumbnailDataSource.saveThumbnail("invalid_id", invalidInputStream)

        // Then - result should be null
        assertNull(thumbnail)
    }

    @Test
    fun deleteThumbnail_deletesFile_whenExists() = runTest {
        val context = getApplicationContext<Context>()
        // Given - a saved file
        thumbnail = File(context.filesDir, "img_test_id.png")
        val thumbnail = thumbnail
        thumbnail!!.createNewFile()
        assertTrue(thumbnail.exists())

        // When - deleting the thumbnail
        thumbnailDataSource.deleteThumbnail(thumbnail.name)

        // Then - file should no longer exist
        assertFalse(thumbnail.exists())
    }

    private fun Bitmap.toInputStream(): InputStream {
        val outputStream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return ByteArrayInputStream(outputStream.toByteArray())
    }
}