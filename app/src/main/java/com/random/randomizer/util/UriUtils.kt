package com.random.randomizer.util

import android.content.Context
import android.net.Uri
import java.io.InputStream

fun Uri.getInputStreamOrNull(context: Context): InputStream? {
    return runCatching { context.contentResolver.openInputStream(this) }
        .onFailure { it.printStackTrace() }
        .getOrNull()
}

fun Uri.getUniqueFilename(): String {
    return lastPathSegment ?: System.currentTimeMillis().toString()
}