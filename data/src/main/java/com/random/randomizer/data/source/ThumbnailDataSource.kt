package com.random.randomizer.data.source

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject

class ThumbnailDataSource @Inject constructor(@ApplicationContext context: Context) {
    private val saveDirectory = context.filesDir

    fun saveThumbnail(id: String, imageInputStream: InputStream): File? {
        require(!id.contains(ProhibitedSymbolRegex)) {
            "id must not contain prohibited symbols"
        }
        return runCatching {
            imageInputStream.use { stream ->
                val bitmap = decodeBitmapFromStream(stream) ?: return null
                return saveBitmapToFile(
                    bitmap = bitmap,
                    directory = saveDirectory,
                    filename = "img_$id.png"
                )
            }
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()
    }

    private fun decodeBitmapFromStream(inputStream: InputStream): Bitmap? {
        return BitmapFactory.decodeStream(inputStream)
    }

    private fun saveBitmapToFile(bitmap: Bitmap, directory: File, filename: String): File? {
        val file = File(directory, filename)
        FileOutputStream(file).use { output ->
            val isCompressed = bitmap.compress(Bitmap.CompressFormat.PNG, 100, output)
            return if (isCompressed) {
                file
            } else {
                file.delete()
                null
            }
        }
    }

    fun deleteThumbnail(filename: String) {
        File(saveDirectory, filename).delete()
    }

    private companion object {
        val ProhibitedSymbolRegex = "[\\\\/:*?\"<>|]".toRegex()
    }
}