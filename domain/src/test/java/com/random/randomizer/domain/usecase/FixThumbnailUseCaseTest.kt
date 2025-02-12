package com.random.randomizer.domain.usecase

import com.random.randomizer.domain.model.Image
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class FixThumbnailUseCaseTest {

    private val fixThumbnailUseCase = FixThumbnailUseCase()

    @Test
    fun replacesThumbnailId() = runTest {
        val thumbnail = Image("id\\with\\separators.png", byteArrayOf())
        val actual = fixThumbnailUseCase(thumbnail)
        val expected = thumbnail.copy(id = "separators.png")
        assertEquals(expected, actual)
    }
}