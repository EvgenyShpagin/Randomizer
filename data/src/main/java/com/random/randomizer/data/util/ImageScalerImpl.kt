package com.random.randomizer.data.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.random.randomizer.domain.model.Image
import com.random.randomizer.domain.util.ImageScaler
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import kotlin.math.min
import kotlin.math.roundToInt

class ImageScalerImpl @Inject constructor() : ImageScaler {

    override fun scale(image: Image, minSideSize: Int): Image {
        require(minSideSize > 0) { "minSideSize must be greater than 0, was $minSideSize" }

        val imageBitmap = image.toBitmap()

        val smallerSideSize = min(imageBitmap.width, imageBitmap.height).toFloat()
        if (smallerSideSize <= minSideSize) {
            return image.copy(data = imageBitmap.toByteArray())
        }

        val scale = minSideSize / smallerSideSize

        val newWidth = (imageBitmap.width * scale).roundToInt()
        val newHeight = (imageBitmap.height * scale).roundToInt()
        val scaledImageBitmap = Bitmap.createScaledBitmap(imageBitmap, newWidth, newHeight, true)
        return image.copy(data = scaledImageBitmap.toByteArray())
    }

    //TODO : mb reuse?
    private fun Image.toBitmap(): Bitmap {
        return BitmapFactory.decodeByteArray(data, 0, data.size)
    }

    private fun Bitmap.toByteArray(): ByteArray {
        val outputStream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }
}