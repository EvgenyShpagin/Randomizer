package com.random.randomizer.domain.usecase

import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.repository.WheelSegmentRepository
import javax.inject.Inject

class CreateWheelSegmentUseCase @Inject constructor(
    private val wheelSegmentRepository: WheelSegmentRepository
) {
    suspend operator fun invoke(wheelSegment: WheelSegment) {
        wheelSegmentRepository.add(wheelSegment)
    }
}