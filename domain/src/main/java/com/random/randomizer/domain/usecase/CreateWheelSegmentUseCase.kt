package com.random.randomizer.domain.usecase

import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.repository.WheelSegmentRepository
import java.io.File
import javax.inject.Inject

class CreateWheelSegmentUseCase @Inject constructor(
    private val wheelSegmentRepository: WheelSegmentRepository
) {
    suspend operator fun invoke(wheelSegment: WheelSegment): Int {
        return wheelSegmentRepository.add(wheelSegment.withFixedThumbnail())
    }

    private fun WheelSegment.withFixedThumbnail(): WheelSegment {
        return if (thumbnail == null) {
            this
        } else {
            val correctThumbnailId = thumbnail.id.substringAfterLast(File.separator)
            val correctThumbnail = thumbnail.copy(id = correctThumbnailId)
            copy(thumbnail = correctThumbnail)
        }
    }
}