package com.random.randomizer.domain.usecase

import com.random.randomizer.data.repository.FakeWheelSegmentRepository
import com.random.randomizer.domain.model.Image
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.repository.WheelSegmentRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test

class CreateWheelSegmentUseCaseTest {

    private lateinit var repository: WheelSegmentRepository
    private lateinit var createWheelSegmentUseCase: CreateWheelSegmentUseCase

    private val wheelSegment = WheelSegment(
        id = 0,
        title = "Title",
        description = "Description",
        thumbnail = Image("id\\with\\separators.png", byteArrayOf()),
        customColor = null
    )

    @Before
    fun setup() {
        repository = FakeWheelSegmentRepository()
        createWheelSegmentUseCase = CreateWheelSegmentUseCase(FixThumbnailUseCase(), repository)
    }

    @Test
    fun addsSegment() = runTest {
        createWheelSegmentUseCase(wheelSegment)
        assertEquals(repository.getAll().count(), 1)
    }

    @Test
    fun replacesId() = runTest {
        val createdSegmentId = createWheelSegmentUseCase(wheelSegment)
        assertNotEquals(wheelSegment.id, createdSegmentId)
    }
}