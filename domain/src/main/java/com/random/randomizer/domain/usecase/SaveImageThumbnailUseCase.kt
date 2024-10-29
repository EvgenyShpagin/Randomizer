package com.random.randomizer.domain.usecase

import com.random.randomizer.domain.common.Result
import java.io.InputStream
import javax.inject.Inject

// TODO: implement
class SaveImageThumbnailUseCase @Inject constructor() {
    suspend operator fun invoke(imageId: String, inputStream: InputStream): Result<String, Error> {
        return Result.Failure(Error())
    }
}
