package com.random.randomizer.domain.repository

import java.io.File
import java.io.InputStream

interface ThumbnailRepository {
    suspend fun save(imageId: String, imageInputStream: InputStream): File?
    suspend fun delete(thumbnailPath: String)
}