package com.random.randomizer.domain.usecase

import com.random.randomizer.domain.common.Result
import com.random.randomizer.domain.error.SaveImageThumbnailError
import com.random.randomizer.domain.repository.ThumbnailRepository
import java.io.InputStream
import javax.inject.Inject

class SaveImageThumbnailUseCase @Inject constructor(
    private val thumbnailRepository: ThumbnailRepository
) {
    suspend operator fun invoke(
        imageId: String,
        imageInputStream: InputStream
    ): Result<String, SaveImageThumbnailError> {
        val imageFile = thumbnailRepository.save(imageId, imageInputStream)
        return if (imageFile != null) {
            Result.Success(data = imageFile.path)
        } else {
            Result.Failure(error = SaveImageThumbnailError.UnableToSaveFile)
        }
    }
}
