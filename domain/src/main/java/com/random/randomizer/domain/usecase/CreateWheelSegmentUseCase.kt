package com.random.randomizer.domain.usecase

import com.random.randomizer.domain.common.Result
import com.random.randomizer.domain.error.CreateWheelSegmentError
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.repository.WheelSegmentRepository
import javax.inject.Inject

class CreateWheelSegmentUseCase @Inject constructor(
    private val fixThumbnailUseCase: FixThumbnailUseCase,
    private val wheelSegmentRepository: WheelSegmentRepository
) {
    suspend operator fun invoke(wheelSegment: WheelSegment): Result<Int, CreateWheelSegmentError> {
        val wheelSegmentToSave = wheelSegment.withFixedThumbnail()
        val id = wheelSegmentRepository.add(wheelSegmentToSave)
        val savedWheelSegment = wheelSegmentRepository.get(id)
        if (wheelSegmentToSave.thumbnail != null && savedWheelSegment?.thumbnail == null) {
            return Result.Failure(CreateWheelSegmentError.FailedToSaveThumbnail)
        }
        return Result.Success(id)
    }

    private fun WheelSegment.withFixedThumbnail(): WheelSegment {
        return if (thumbnail == null) {
            this
        } else {
            copy(thumbnail = fixThumbnailUseCase(thumbnail))
        }
    }
}