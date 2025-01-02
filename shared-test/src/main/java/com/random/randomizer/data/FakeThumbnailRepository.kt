package com.random.randomizer.data

import androidx.annotation.VisibleForTesting
import com.random.randomizer.domain.repository.ThumbnailRepository
import java.io.File
import java.io.InputStream
import javax.inject.Inject

class FakeThumbnailRepository @Inject constructor() : ThumbnailRepository {

    val savedImages = mutableListOf<File>()

    override suspend fun save(
        imageId: String,
        imageInputStream: InputStream
    ): File? {
        return runCatching {
            File(imageId).apply { createNewFile() }
        }.onSuccess { file ->
            savedImages.add(file)
        }.getOrNull()
    }

    override suspend fun delete(thumbnailPath: String) {
        savedImages.find { it.name == thumbnailPath }?.let { file ->
            file.delete()
            savedImages.remove(file)
        }
    }

    @VisibleForTesting
    fun deleteAllFiles() {
        savedImages.forEach { file ->
            file.delete()
        }
        savedImages.clear()
    }
}