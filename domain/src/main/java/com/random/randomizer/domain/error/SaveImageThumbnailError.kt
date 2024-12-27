package com.random.randomizer.domain.error

sealed class SaveImageThumbnailError : Error() {
    data object UnableToSaveFile : SaveImageThumbnailError()
}