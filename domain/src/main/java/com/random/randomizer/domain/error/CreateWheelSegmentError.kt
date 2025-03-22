package com.random.randomizer.domain.error

import com.random.randomizer.domain.common.Error

sealed class CreateWheelSegmentError : Error {
    data object FailedToSaveThumbnail : CreateWheelSegmentError()
}