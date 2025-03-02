package com.random.randomizer.domain.usecase

import com.random.randomizer.data.repository.FakeWheelSegmentRepository
import com.random.randomizer.domain.model.Image
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.repository.WheelSegmentRepository
import com.random.randomizer.domain.util.ImageScaler
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class UpdateWheelSegmentUseCaseTest {

    private lateinit var repository: WheelSegmentRepository
    private val imageScaler = mockk<ImageScaler>()

    private val wheelSegment = WheelSegment(
        id = 1,
        title = "Title",
        description = "Description",
        thumbnail = Image("id\\with\\separators.png", byteArrayOf()),
        customColor = null
    )

    // Subject under test
    private lateinit var updateWheelSegmentUseCase: UpdateWheelSegmentUseCase

    @Before
    fun setup() {
        repository = FakeWheelSegmentRepository()
        updateWheelSegmentUseCase = UpdateWheelSegmentUseCase(
            FixThumbnailUseCase(), repository, imageScaler
        )
    }

    @Test
    fun throwsException_whenSegmentDoesNotExist() = runTest {
        // Given - empty repository

        // When - invoke update
        assertThrows(NullPointerException::class.java) {
            runBlocking {
                updateWheelSegmentUseCase(wheelSegment)
            }
        }
    }

    @Test
    fun updatesSegment_whenSegmentExists() = runTest {
        // Given - one item in repository
        repository.add(wheelSegment)

        val changedWheelSegment = wheelSegment.copy(title = "Other title")

        // When - invoke update
        updateWheelSegmentUseCase(changedWheelSegment)

        // Then - verify item updated
        val actual = repository.get(wheelSegment.id)
        assertEquals(changedWheelSegment, actual)
    }

    @Test
    fun doesNotScale_whenImageIsTheSame() = runTest {
        // Given - one item in repository
        repository.add(wheelSegment)

        // When - invoke update
        updateWheelSegmentUseCase(wheelSegment)

        // Then - verify scale was not called
        coVerify(exactly = 0) { imageScaler.scale(any(), any()) }
    }

    @Test
    fun scale_whenImageWasReplaced() = runTest {
        // Given - one item in repository
        repository.add(wheelSegment)
        val thumbnail = wheelSegment.thumbnail!!
        val changedWheelSegment = wheelSegment.copy(thumbnail = thumbnail.copy(id = "otherId.png"))

        coEvery { imageScaler.scale(any(), any()) } returns thumbnail

        // When - invoke update
        updateWheelSegmentUseCase(changedWheelSegment)

        // Then - verify scale was called
        coVerify { imageScaler.scale(any(), any()) }
    }
}