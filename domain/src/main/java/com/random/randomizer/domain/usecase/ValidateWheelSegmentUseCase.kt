package com.random.randomizer.domain.usecase

import com.random.randomizer.domain.common.Result
import com.random.randomizer.domain.error.WheelSegmentValidationError
import com.random.randomizer.domain.error.WheelSegmentValidationError.AlreadyExists
import com.random.randomizer.domain.error.WheelSegmentValidationError.Empty
import com.random.randomizer.domain.model.Image
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.repository.WheelSegmentRepository
import java.io.File
import javax.inject.Inject

class ValidateWheelSegmentUseCase @Inject constructor(
    private val wheelSegmentRepository: WheelSegmentRepository
) {
    suspend operator fun invoke(
        wheelSegment: WheelSegment
    ): Result<Unit, WheelSegmentValidationError> {
        val allSegments = wheelSegmentRepository.getAll()
        val isDuplicate = allSegments.any {
            wheelSegment.id != it.id && wheelSegment.contentEquals(it)
        }
        return when {
            isDuplicate -> Result.Failure(AlreadyExists)
            wheelSegment.isEmpty() -> Result.Failure(Empty)
            else -> Result.Success(Unit)
        }
    }

    private fun WheelSegment.isEmpty(): Boolean {
        return title.isBlank()
                && thumbnail == null
                && customColor == null
    }

    private fun WheelSegment.contentEquals(other: WheelSegment): Boolean {
        return this.title == other.title
                && this.description == other.description
                && this.thumbnail.quickEquals(other.thumbnail)
                && this.customColor == other.customColor
    }

    private fun Image?.quickEquals(other: Image?): Boolean {
        return when {
            this == null && other == null -> true
            this == null || other == null -> false
            else -> {
                val fixedThisThumbnailId = this.id.substringAfterLast(File.separator)
                val fixedOtherThumbnailId = other.id.substringAfterLast(File.separator)
                return fixedThisThumbnailId == fixedOtherThumbnailId
            }
        }
    }
}