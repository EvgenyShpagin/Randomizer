package com.random.randomizer.data.util

import android.graphics.Bitmap
import android.util.Size
import androidx.test.filters.SmallTest
import com.random.randomizer.domain.model.Image
import com.random.randomizer.domain.util.ImageScaler
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Test

@SmallTest
class ImageScalerTest {

    // Subject under test
    private val imageScaler: ImageScaler = ImageScalerImpl()

    @Test
    fun scaleToSize_returnsScaledBitmap_whenScalingLower() {
        // Given - a large bitmap as input
        val image = createImage500x1000()

        // When - invoked with correct data
        val actualImage = imageScaler.scale(image, 100)
        val actualBitmap = actualImage.toBitmap()

        // Then - verify bitmap is non-null and scaled to 1/5
        assertNotNull(actualImage)

        val actualSize = Size(actualBitmap.width, actualBitmap.height)
        val expectedSize = Size(/* width = */ 100, /* height = */ 200)

        assertEquals(expectedSize, actualSize)
    }

    @Test
    fun scaleToSize_returnsOriginalBitmap_whenScalingUpper() {
        // Given - a large bitmap as input
        val image = createImage500x1000()

        // When - invoked with correct Bitmap and enlarged side size
        val actualImage = imageScaler.scale(image, 2000)
        val actualBitmap = actualImage.toBitmap()

        // Then - verify bitmap is non-null and scaled to 1/5
        assertNotNull(actualBitmap)

        val actualSize = Size(actualBitmap.width, actualBitmap.height)
        val expectedSize = Size(/* width = */ 500, /* height = */ 1000)

        assertEquals(expectedSize, actualSize)
    }

    @Test
    fun scaleToSize_returnsOriginalBitmap_whenScalingToSameSize() {
        // Given - a large bitmap as input
        val image = createImage500x1000()

        // When - invoked with correct Bitmap and reduced side size
        val actualImage = imageScaler.scale(image, 500)
        val actualBitmap = actualImage.toBitmap()

        // Then - verify bitmap is non-null and scaled to 1/5
        assertNotNull(actualBitmap)

        val actualSize = Size(actualBitmap.width, actualBitmap.height)
        val expectedSize = Size(/* width = */ 500, /* height = */ 1000)

        assertEquals(expectedSize, actualSize)
    }

    private fun createImage500x1000(): Image {
        val bitmap = Bitmap.createBitmap(500, 1000, Bitmap.Config.ARGB_8888)
        return Image("id", bitmap.toByteArray())
    }
}