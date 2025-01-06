package com.random.randomizer.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import kotlin.math.min
import kotlin.math.roundToInt

fun ImageBitmap.Companion.decodeFile(pathName: String): ImageBitmap? {
    return BitmapFactory.decodeFile(pathName)?.asImageBitmap()
}

fun ImageBitmap.toInputStream(): InputStream {
    val outputStream = ByteArrayOutputStream()
    asAndroidBitmap().compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    return ByteArrayInputStream(outputStream.toByteArray())
}

fun ImageBitmap.scaleToSize(minSideSize: Int): ImageBitmap {
    require(minSideSize > 0) { "minSideSize must be greater than 0, was $minSideSize" }

    val smallerSideSize = min(this.width, this.height).toFloat()
    if (smallerSideSize <= minSideSize) return this

    val scale = minSideSize / smallerSideSize

    val newWidth = (width * scale).roundToInt()
    val newHeight = (height * scale).roundToInt()
    return Bitmap
        .createScaledBitmap(asAndroidBitmap(), newWidth, newHeight, true)
        .asImageBitmap()
}