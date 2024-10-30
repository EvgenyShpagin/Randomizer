package com.random.randomizer.domain.usecase

import javax.inject.Inject

// TODO: implement
class DeleteWheelSegmentUseCase @Inject constructor(
    private val deleteThumbnailUseCase: DeleteThumbnailUseCase
) {
    suspend operator fun invoke(wheelSegmentId: Int) = Unit
}