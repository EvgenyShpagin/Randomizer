package com.random.randomizer.domain.usecase

import com.random.randomizer.domain.common.Result
import com.random.randomizer.domain.error.UpdateWheelSegmentError
import com.random.randomizer.domain.error.UpdateWheelSegmentError.FailedToSaveThumbnail
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.repository.WheelSegmentRepository
import com.random.randomizer.domain.util.ImageScaler
import javax.inject.Inject

class UpdateWheelSegmentUseCase @Inject constructor(
    private val fixThumbnailUseCase: FixThumbnailUseCase,
    private val wheelSegmentRepository: WheelSegmentRepository,
    private val imageScaler: ImageScaler
) {
    suspend operator fun invoke(
        wheelSegment: WheelSegment
    ): Result<Unit, UpdateWheelSegmentError> {
        val currentWheelSegment = wheelSegmentRepository.get(wheelSegment.id)!!

        if (hasImageBeenReplaced(currentWheelSegment, wheelSegment)) {
            val scaledImage = imageScaler.scale(wheelSegment.thumbnail!!, 600)
            val fixedImage = fixThumbnailUseCase(scaledImage)
            wheelSegmentRepository.update(wheelSegment.copy(thumbnail = fixedImage))
        } else {
            wheelSegmentRepository.update(wheelSegment)
        }

        // Assert non-null as it was checked earlier
        val actualWheelSegment = wheelSegmentRepository.get(wheelSegment.id)!!
        if (wheelSegment.thumbnail != null && actualWheelSegment.thumbnail == null) {
            return Result.Failure(FailedToSaveThumbnail)
        }

        return Result.Success(Unit)
    }

    private fun hasImageBeenReplaced(
        currentSegment: WheelSegment,
        newSegment: WheelSegment
    ): Boolean {
        val current = currentSegment.thumbnail
        val new = newSegment.thumbnail
        return current != null && new != null && current.id != new.id
    }
}