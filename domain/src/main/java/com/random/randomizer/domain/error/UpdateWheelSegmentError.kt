package com.random.randomizer.domain.error

sealed class UpdateWheelSegmentError : Error() {
    data object FailedToSaveThumbnail : UpdateWheelSegmentError()
}