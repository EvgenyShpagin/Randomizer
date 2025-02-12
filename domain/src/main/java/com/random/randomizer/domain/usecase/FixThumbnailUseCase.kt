package com.random.randomizer.domain.usecase

import com.random.randomizer.domain.model.Image
import java.io.File
import javax.inject.Inject

class FixThumbnailUseCase @Inject constructor() {
    operator fun invoke(thumbnail: Image): Image {
        val correctThumbnailId = thumbnail.id.substringAfterLast(File.separator)
        return thumbnail.copy(id = correctThumbnailId)
    }
}
