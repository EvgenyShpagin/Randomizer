package com.random.randomizer.domain.usecase

import com.random.randomizer.domain.model.WheelSegment
import javax.inject.Inject

// TODO: implement
class UpdateWheelSegmentUseCase @Inject constructor() {

    suspend operator fun invoke(wheelSegmentId: Int, transform: (WheelSegment) -> WheelSegment) {}
}