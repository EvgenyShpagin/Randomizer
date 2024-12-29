package com.random.randomizer.domain.usecase

import com.random.randomizer.domain.repository.WheelSegmentRepository
import javax.inject.Inject

class DeleteWheelSegmentUseCase @Inject constructor(
    private val wheelSegmentRepository: WheelSegmentRepository,
    private val deleteThumbnailUseCase: DeleteThumbnailUseCase
) {
    suspend operator fun invoke(wheelSegmentId: Int) {
        val wheelSegmentToDelete = wheelSegmentRepository.get(wheelSegmentId) ?: return
        wheelSegmentRepository.deleteById(wheelSegmentId)
        wheelSegmentToDelete.thumbnailPath?.let {
            deleteThumbnailUseCase(it)
        }
    }
}