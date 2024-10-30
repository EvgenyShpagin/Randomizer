package com.random.randomizer.domain.usecase

import javax.inject.Inject

// TODO: Implement
class MakeWheelSegmentUniqueUseCase @Inject constructor() {
    suspend operator fun invoke(wheelSegmentId: Int) = Unit
}