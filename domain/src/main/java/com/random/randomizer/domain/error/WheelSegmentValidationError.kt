package com.random.randomizer.domain.error

sealed class WheelSegmentValidationError : Error() {
    data object AlreadyExists : WheelSegmentValidationError()
    data object Empty : WheelSegmentValidationError()
}