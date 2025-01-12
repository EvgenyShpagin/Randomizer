package com.random.randomizer.domain.usecase

import com.random.randomizer.domain.repository.WheelSegmentRepository
import javax.inject.Inject

class DeleteWheelSegmentUseCase @Inject constructor(
    private val wheelSegmentRepository: WheelSegmentRepository
) {
    suspend operator fun invoke(wheelSegmentId: Int) {
        wheelSegmentRepository.deleteById(wheelSegmentId)
    }
}