package com.random.randomizer.domain.usecase

import com.random.randomizer.domain.common.Result
import com.random.randomizer.domain.error.WheelSegmentValidationError
import com.random.randomizer.domain.error.WheelSegmentValidationError.AlreadyExists
import com.random.randomizer.domain.error.WheelSegmentValidationError.Empty
import com.random.randomizer.domain.model.WheelSegment
import javax.inject.Inject

// TODO: implement
class ValidateWheelSegmentUseCase @Inject constructor() {

    suspend operator fun invoke(
        wheelSegment: WheelSegment
    ): Result<Unit, WheelSegmentValidationError> {
        val allSegments = listOf(wheelSegment) // stub
        val isDuplicate = allSegments.any {
            wheelSegment.id != it.id && wheelSegment.contentEquals(it)
        }
        return when {
            isDuplicate -> Result.Failure(AlreadyExists)
            wheelSegment.isEmpty() -> Result.Failure(Empty)
            else -> Result.Success(Unit)
        }
    }

    private fun WheelSegment.contentEquals(other: WheelSegment): Boolean {
        return this.title == other.title
                && this.description == other.description
                && this.thumbnailPath == other.thumbnailPath
                && this.customColor == other.customColor
    }

    private fun WheelSegment.isEmpty(): Boolean {
        return title.isBlank()
                && thumbnailPath == null
                && customColor == null
    }
}