package com.random.randomizer.domain.usecase

import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.repository.WheelSegmentRepository
import javax.inject.Inject

class GetWheelSegmentUseCase @Inject constructor(private val repository: WheelSegmentRepository) {
    suspend operator fun invoke(wheelSegmentId: Int): WheelSegment? {
        return repository.get(wheelSegmentId)
    }
}