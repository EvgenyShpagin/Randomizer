package com.random.randomizer.data.source

import androidx.room.TypeConverter
import com.random.randomizer.domain.model.Image

/**
 * Converters allowing store only id of [Image].
 * The image itself is not contained in the database, only its identifier
 */
class ImageConverter {

    @TypeConverter
    fun imageToString(image: Image): String {
        return image.id
    }

    @TypeConverter
    fun imageFromString(imageId: String): Image {
        return Image(imageId, byteArrayOf())
    }
}