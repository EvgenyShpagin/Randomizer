package com.random.randomizer.util

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

fun ImageBitmap.Companion.decodeFile(pathName: String): ImageBitmap? {
    return BitmapFactory.decodeFile(pathName)?.asImageBitmap()
}