package com.random.randomizer.data.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.random.randomizer.domain.model.Image

/**
 * Converter from [Image] to [Bitmap]
 *
 * @return Bitmap if the image data is compressed bitmap
 */
fun Image.toBitmap(): Bitmap {
    return BitmapFactory.decodeByteArray(data, 0, data.size)
}