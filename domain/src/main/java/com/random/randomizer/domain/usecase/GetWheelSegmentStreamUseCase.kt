package com.random.randomizer.domain.usecase

import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.repository.WheelSegmentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWheelSegmentStreamUseCase @Inject constructor(
    private val wheelSegmentRepository: WheelSegmentRepository
) {
    operator fun invoke(wheelSegmentId: Int): Flow<WheelSegment> {
        return wheelSegmentRepository.getStream(wheelSegmentId)
    }
}