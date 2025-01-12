package com.random.randomizer.util

import android.content.Context
import android.net.Uri

fun Uri.toByteArray(context: Context): ByteArray? {
    return runCatching { context.contentResolver.openInputStream(this) }
        .onFailure { it.printStackTrace() }
        .getOrNull()
        ?.readBytes()
}

fun Uri.getUniqueFilename(): String {
    return lastPathSegment ?: System.currentTimeMillis().toString()
}