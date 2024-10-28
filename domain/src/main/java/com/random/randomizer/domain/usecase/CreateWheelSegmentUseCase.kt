package com.random.randomizer.domain.usecase

import com.random.randomizer.domain.model.WheelSegment
import javax.inject.Inject

// TODO: implement
class CreateWheelSegmentUseCase @Inject constructor() {

    suspend operator fun invoke(): WheelSegment {
        return WheelSegment(-1, "", "", null, null)
    }
}