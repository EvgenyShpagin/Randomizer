package com.random.randomizer.domain.usecase

import com.random.randomizer.domain.repository.ThumbnailRepository
import javax.inject.Inject

class DeleteThumbnailUseCase @Inject constructor(
    private val thumbnailRepository: ThumbnailRepository
) {
    suspend operator fun invoke(thumbnailPath: String) {
        thumbnailRepository.delete(thumbnailPath = thumbnailPath)
    }
}