package com.random.randomizer.data.repository

import com.random.randomizer.domain.repository.ThumbnailRepository
import java.io.File
import java.io.InputStream
import javax.inject.Inject

class ThumbnailRepositoryImpl @Inject constructor() : ThumbnailRepository {
    override suspend fun save(
        imageId: String,
        imageInputStream: InputStream
    ): File? {
        return null
    }

    override suspend fun delete(thumbnailPath: String) {}
}