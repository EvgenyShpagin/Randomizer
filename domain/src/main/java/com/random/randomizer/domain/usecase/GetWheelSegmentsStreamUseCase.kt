package com.random.randomizer.domain.usecase

import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.repository.WheelSegmentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWheelSegmentsStreamUseCase @Inject constructor(
    private val wheelSegmentRepository: WheelSegmentRepository
) {
    operator fun invoke(): Flow<List<WheelSegment>> {
        return wheelSegmentRepository.getAllStream()
    }
}