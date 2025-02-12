package com.random.randomizer.domain.error

sealed class CreateWheelSegmentError : Error() {
    data object FailedToSaveThumbnail : CreateWheelSegmentError()
}