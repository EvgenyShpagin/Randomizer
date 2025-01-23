package com.random.randomizer.domain.usecase

import com.random.randomizer.domain.common.Result
import com.random.randomizer.domain.error.UpdateWheelSegmentError
import com.random.randomizer.domain.error.UpdateWheelSegmentError.FailedToSaveThumbnail
import com.random.randomizer.domain.error.UpdateWheelSegmentError.WheelSegmentDoesNotExist
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.repository.WheelSegmentRepository
import com.random.randomizer.domain.util.ImageScaler
import javax.inject.Inject

class UpdateWheelSegmentUseCase @Inject constructor(
    private val wheelSegmentRepository: WheelSegmentRepository,
    private val imageScaler: ImageScaler
) {
    suspend operator fun invoke(
        wheelSegmentId: Int,
        transform: (WheelSegment) -> WheelSegment
    ): Result<Unit, UpdateWheelSegmentError> {
        val currentWheelSegment = wheelSegmentRepository.get(wheelSegmentId)
            ?: return Result.Failure(WheelSegmentDoesNotExist)

        val expectedWheelSegment = transform(currentWheelSegment)

        if (hasImageBeenReplaced(currentWheelSegment, expectedWheelSegment)) {
            val scaledImage = imageScaler.scale(expectedWheelSegment.thumbnail!!, 600)
            wheelSegmentRepository.update(expectedWheelSegment.copy(thumbnail = scaledImage))
        } else {
            wheelSegmentRepository.update(expectedWheelSegment)
        }

        // Assert non-null as it was checked earlier
        val actualWheelSegment = wheelSegmentRepository.get(wheelSegmentId)!!
        if (expectedWheelSegment.thumbnail != null && actualWheelSegment.thumbnail == null) {
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