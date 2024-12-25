package com.random.randomizer.domain.usecase

import com.random.randomizer.domain.common.Result
import com.random.randomizer.domain.error.UpdateWheelSegmentError
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.repository.WheelSegmentRepository
import javax.inject.Inject

class UpdateWheelSegmentUseCase @Inject constructor(
    private val wheelSegmentRepository: WheelSegmentRepository
) {
    suspend operator fun invoke(
        wheelSegmentId: Int,
        transform: (WheelSegment) -> WheelSegment
    ): Result<Unit, UpdateWheelSegmentError> {
        val currentWheelSegment = wheelSegmentRepository.get(wheelSegmentId)
            ?: return Result.Failure(UpdateWheelSegmentError.WheelSegmentDoesNotExist)

        val updatedWheelSegment = transform(currentWheelSegment)
        wheelSegmentRepository.update(updatedWheelSegment)

        return Result.Success(Unit)
    }
}