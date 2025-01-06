package com.random.randomizer.util

import android.util.Size
import androidx.compose.ui.graphics.ImageBitmap
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Test

class BitmapUtilTest {

    @Test
    fun scaleToSize_returnsScaledBitmap_whenScalingLower() {
        // Given - a large bitmap as input
        val srcHeight = 500
        val srcWidth = 1000
        val bitmap = ImageBitmap(srcWidth, srcHeight)

        // When - invoked with correct data
        val actualBitmap = bitmap.scaleToSize(100)

        // Then - verify bitmap is non-null and scaled to 1/5
        assertNotNull(actualBitmap)

        val actualSize = Size(actualBitmap.width, actualBitmap.height)
        val expectedSize = Size(/* width = */ srcWidth / 5, /* height = */ srcHeight / 5)

        assertEquals(expectedSize, actualSize)
    }

    @Test
    fun scaleToSize_returnsOriginalBitmap_whenScalingUpper() {
        // Given - a large bitmap as input
        val srcHeight = 500
        val srcWidth = 1000
        val bitmap = ImageBitmap(srcWidth, srcHeight)

        // When - invoked with correct Bitmap and enlarged side size
        val actualBitmap = bitmap.scaleToSize(2000)

        // Then - verify bitmap is non-null and scaled to 1/5
        assertNotNull(actualBitmap)

        val actualSize = Size(actualBitmap.width, actualBitmap.height)
        val expectedSize = Size(/* width = */ srcWidth, /* height = */ srcHeight)

        assertEquals(expectedSize, actualSize)
    }

    @Test
    fun scaleToSize_returnsOriginalBitmap_whenScalingToSameSize() {
        // Given - a large bitmap as input
        val srcHeight = 500
        val srcWidth = 1000
        val bitmap = ImageBitmap(srcWidth, srcHeight)

        // When - invoked with correct Bitmap and reduced side size
        val actualBitmap = bitmap.scaleToSize(500)

        // Then - verify bitmap is non-null and scaled to 1/5
        assertNotNull(actualBitmap)

        val actualSize = Size(actualBitmap.width, actualBitmap.height)
        val expectedSize = Size(/* width = */ srcWidth, /* height = */ srcHeight)

        assertEquals(expectedSize, actualSize)
    }
}