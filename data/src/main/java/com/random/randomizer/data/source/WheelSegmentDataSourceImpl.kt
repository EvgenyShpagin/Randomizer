package com.random.randomizer.data.source

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat.PNG
import com.random.randomizer.data.model.WheelSegment
import com.random.randomizer.data.util.toBitmap
import com.random.randomizer.domain.model.Image
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class WheelSegmentDataSourceImpl @Inject constructor(
    @ApplicationContext context: Context,
    private val wheelSegmentDao: WheelSegmentDao
) : WheelSegmentDataSource {
    private val imageSaveDirectory = context.filesDir

    override suspend fun getById(id: Int): WheelSegment? {
        val wheelSegment = wheelSegmentDao.getById(id)
        return wheelSegment?.withLoadedThumbnail()
    }

    private fun WheelSegment.withLoadedThumbnail(): WheelSegment {
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

    override suspend fun insert(wheelSegment: WheelSegment): Long {
        wheelSegment.thumbnail?.let { saveThumbnail(it) }
        return wheelSegmentDao.insert(wheelSegment.withoutId())
    }

    private fun WheelSegment.withoutId(): WheelSegment {
        return if (id == 0) {
            this
        } else {
            copy(id = 0)
        }
    }

    override suspend fun update(wheelSegment: WheelSegment) {
        val currentWheelSegment = getById(wheelSegment.id) ?: return

        when {
            isThumbnailChanged(currentWheelSegment, wheelSegment) -> {
                wheelSegment.thumbnail?.let { saveThumbnail(it) }
                wheelSegmentDao.update(wheelSegment)
            }

            isThumbnailRemoved(currentWheelSegment, wheelSegment) -> {
                wheelSegmentDao.update(wheelSegment)
                deleteThumbnailIfUnused(currentWheelSegment.thumbnail!!)
            }

            else -> wheelSegmentDao.update(wheelSegment)
        }
    }

    private fun isThumbnailRemoved(
        currentWheelSegment: WheelSegment?,
        newWheelSegment: WheelSegment
    ): Boolean {
        return currentWheelSegment?.thumbnail != null && newWheelSegment.thumbnail == null
    }

    private fun isThumbnailChanged(
        currentWheelSegment: WheelSegment,
        newWheelSegment: WheelSegment
    ): Boolean {
        return newWheelSegment.thumbnail != null &&
                currentWheelSegment.thumbnail != newWheelSegment.thumbnail
    }

    private fun saveThumbnail(image: Image): Boolean {
        require(!image.id.contains(ProhibitedSymbolRegex)) {
            "id must not contain prohibited symbols"
        }
        val bitmap = image.toBitmap()
        return saveBitmap(bitmap, image.id)
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
            wheelSegment.withLoadedThumbnail()
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
        return wheelSegmentDao.observeById(id)
            .filterNotNull()
            .map { wheelSegment ->
                wheelSegment.withLoadedThumbnail()
            }
    }

    override fun observeAll(): Flow<List<WheelSegment>> {
        return wheelSegmentDao.observeAll().map { wheelSegments ->
            wheelSegments.map { wheelSegment ->
                wheelSegment.withLoadedThumbnail()
            }
        }
    }

    private companion object {
        val ProhibitedSymbolRegex = "[\\\\/:*?\"<>|]".toRegex()
    }
}