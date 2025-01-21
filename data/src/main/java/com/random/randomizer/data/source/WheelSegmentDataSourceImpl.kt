package com.random.randomizer.data.source

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat.PNG
import com.random.randomizer.data.util.toBitmap
import com.random.randomizer.domain.model.Image
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File
import java.io.FileOutputStream

class WheelSegmentDataSourceImpl(
    context: Context,
    private val wheelSegmentDao: WheelSegmentDao
) : WheelSegmentDataSource {
    private val imageSaveDirectory = context.filesDir

    override suspend fun getById(id: Int): WheelSegment? {
        val wheelSegment = wheelSegmentDao.getById(id)
        return wheelSegment?.withThumbnail()
    }

    private fun WheelSegment.withThumbnail(): WheelSegment {
        if (thumbnail == null) {
            return this
        } else {
            val thumbnail = getThumbnail(thumbnail.id)
            return copy(thumbnail = thumbnail)
        }
    }

    private fun getThumbnail(thumbnailId: String): Image? {
        val imageFile = File(imageSaveDirectory, thumbnailId)
        return if (imageFile.exists()) {
            val thumbnailData = imageFile.readBytes()
            Image(thumbnailId, thumbnailData)
        } else {
            null
        }
    }

    override suspend fun upsert(wheelSegment: WheelSegment): Long {
        val currentWheelSegment = getById(wheelSegment.id)

        return when {
            isThumbnailChanged(currentWheelSegment, wheelSegment) -> {
                val savedThumbnail = saveThumbnail(wheelSegment.thumbnail!!)
                    ?: throw IllegalStateException("Failed to save image")
                val wheelSegmentWithSavedThumbnail = wheelSegment.copy(thumbnail = savedThumbnail)
                wheelSegmentDao.upsert(wheelSegmentWithSavedThumbnail)
            }

            isThumbnailRemoved(currentWheelSegment, wheelSegment) -> {
                wheelSegmentDao.upsert(wheelSegment)
                    .also { deleteThumbnailIfUnused(currentWheelSegment!!.thumbnail!!) }
            }

            else -> wheelSegmentDao.upsert(wheelSegment)
        }
    }

    private fun isThumbnailRemoved(
        currentWheelSegment: WheelSegment?,
        newWheelSegment: WheelSegment
    ): Boolean {
        return currentWheelSegment?.thumbnail != null && newWheelSegment.thumbnail == null
    }

    private fun isThumbnailChanged(
        currentWheelSegment: WheelSegment?,
        newWheelSegment: WheelSegment
    ): Boolean {
        return newWheelSegment.thumbnail != null &&
                currentWheelSegment?.thumbnail != newWheelSegment.thumbnail
    }

    private fun saveThumbnail(image: Image): Image? {
        require(!image.id.contains(ProhibitedSymbolRegex)) {
            "id must not contain prohibited symbols"
        }
        val bitmap = image.toBitmap()
        val filename = image.id.withExtension()
        val isSaved = saveBitmap(bitmap, filename)
        return if (isSaved) {
            image.copy(id = filename)
        } else {
            null
        }
    }

    private fun String.withExtension(): String {
        if (endsWith(".png")) return this
        return "$this.png"
    }

    private fun saveBitmap(bitmap: Bitmap, filename: String): Boolean {
        val file = File(imageSaveDirectory, filename)
        runCatching { FileOutputStream(file) }
            .onFailure { it.printStackTrace() }
            .onSuccess {
                it.use { output ->
                    val isCompressed = bitmap.compress(PNG, 100, output)
                    return isCompressed
                }
            }
        return false
    }

    override suspend fun getAll(): List<WheelSegment> {
        return wheelSegmentDao.getAll().map { wheelSegment ->
            wheelSegment.withThumbnail()
        }
    }

    override suspend fun deleteById(id: Int) {
        val wheelSegment = getById(id) ?: return
        wheelSegmentDao.deleteById(id)
        wheelSegment.thumbnail?.let { deleteThumbnailIfUnused(it) }
    }

    private suspend fun deleteThumbnailIfUnused(thumbnail: Image) {
        val thumbnailIsUsed = getAll().any { it.thumbnail?.id == thumbnail.id }
        if (!thumbnailIsUsed) {
            deleteThumbnail(thumbnail)
        }
    }

    private fun deleteThumbnail(thumbnail: Image): Boolean {
        return File(imageSaveDirectory, thumbnail.id).delete()
    }

    override fun observeById(id: Int): Flow<WheelSegment> {
        return wheelSegmentDao.observeById(id).map { wheelSegment ->
            wheelSegment.withThumbnail()
        }
    }

    override fun observeAll(): Flow<List<WheelSegment>> {
        return wheelSegmentDao.observeAll().map { wheelSegments ->
            wheelSegments.map { wheelSegment ->
                wheelSegment.withThumbnail()
            }
        }
    }

    private companion object {
        val ProhibitedSymbolRegex = "[\\\\/:*?\"<>|]".toRegex()
    }
}