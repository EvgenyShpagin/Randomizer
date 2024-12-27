package com.random.randomizer.domain.usecase

import com.random.randomizer.domain.common.Result
import com.random.randomizer.domain.repository.ThumbnailRepository
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.io.File
import java.io.InputStream

class SaveImageThumbnailUseCaseTest {

    private val repository = mockk<ThumbnailRepository>()

    // Subject under test
    private val saveImageThumbnailUseCase = SaveImageThumbnailUseCase(repository)

    @Test
    fun returnsSuccess_whenSaved() = runTest {
        // Given
        val imageId = "1"
        val imageInputStream = InputStream.nullInputStream()

        coEvery {
            repository.save(imageId, imageInputStream)
        } returns File("\\directory\\img_1.png")

        // When
        val result = saveImageThumbnailUseCase(imageId, imageInputStream)

        // Then
        assertTrue(result is Result.Success)
    }

    @Test
    fun returnsCorrectPath_whenSaved() = runTest {
        // Given
        val imageId = "1"
        val imageInputStream = InputStream.nullInputStream()

        coEvery {
            repository.save(imageId, imageInputStream)
        } returns File("\\directory\\img_1.png")

        // When
        val result = saveImageThumbnailUseCase(imageId, imageInputStream)

        // Then
        assertEquals(
            (result as Result.Success).data,
            "\\directory\\img_1.png"
        )
    }

    @Test
    fun returnsFailure_whenSaveFailed() = runTest {
        // Given
        val imageId = "1"
        val imageInputStream = InputStream.nullInputStream()

        coEvery {
            repository.save(imageId, imageInputStream)
        } returns null

        // When
        val result = saveImageThumbnailUseCase(imageId, imageInputStream)

        // Then
        assertTrue(result is Result.Failure)
    }
}