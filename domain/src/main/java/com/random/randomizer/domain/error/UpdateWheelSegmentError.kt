package com.random.randomizer.domain.error

import com.random.randomizer.domain.common.Error

sealed class UpdateWheelSegmentError : Error {
    data object FailedToSaveThumbnail : UpdateWheelSegmentError()
}