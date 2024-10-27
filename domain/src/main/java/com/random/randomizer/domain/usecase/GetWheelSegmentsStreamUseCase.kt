package com.random.randomizer.domain.usecase

import com.random.randomizer.domain.model.WheelSegment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

// TODO: implement
class GetWheelSegmentsStreamUseCase @Inject constructor() {
    operator fun invoke(): Flow<List<WheelSegment>> {
        return flow {}
    }
}