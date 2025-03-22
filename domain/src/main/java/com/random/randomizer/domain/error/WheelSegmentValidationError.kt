package com.random.randomizer.domain.error

import com.random.randomizer.domain.common.Error

sealed class WheelSegmentValidationError : Error {
    data object AlreadyExists : WheelSegmentValidationError()
    data object Empty : WheelSegmentValidationError()
}