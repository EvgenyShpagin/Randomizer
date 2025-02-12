package com.random.randomizer.domain.usecase

import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.repository.WheelSegmentRepository
import javax.inject.Inject

class CreateWheelSegmentUseCase @Inject constructor(
    private val fixThumbnailUseCase: FixThumbnailUseCase,
    private val wheelSegmentRepository: WheelSegmentRepository
) {
    suspend operator fun invoke(wheelSegment: WheelSegment): Int {
        return wheelSegmentRepository.add(wheelSegment.withFixedThumbnail())
    }

    private fun WheelSegment.withFixedThumbnail(): WheelSegment {
        return if (thumbnail == null) {
            this
        } else {
            copy(thumbnail = fixThumbnailUseCase(thumbnail))
        }
    }
}