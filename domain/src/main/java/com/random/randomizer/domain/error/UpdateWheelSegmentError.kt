package com.random.randomizer.domain.error

sealed class UpdateWheelSegmentError : Error() {
    data object WheelSegmentDoesNotExist : UpdateWheelSegmentError()
    data object FailedToSaveThumbnail : UpdateWheelSegmentError()
}