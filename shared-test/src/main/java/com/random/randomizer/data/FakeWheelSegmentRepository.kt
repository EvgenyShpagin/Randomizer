package com.random.randomizer.data

import androidx.annotation.VisibleForTesting
import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.repository.WheelSegmentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class FakeWheelSegmentRepository @Inject constructor() : WheelSegmentRepository {

    private val wheelSegments = MutableStateFlow(emptyList<WheelSegment>())

    override suspend fun add(segment: WheelSegment) {
        wheelSegments.update { segments -> segments + segment }
    }

    override suspend fun getAll(): List<WheelSegment> {
        return wheelSegments.value
    }

    override fun getAllStream(): Flow<List<WheelSegment>> {
        TODO("Not yet implemented")
    }

    override suspend fun get(segmentId: Int): WheelSegment? {
        TODO("Not yet implemented")
    }

    override fun getStream(segmentId: Int): Flow<WheelSegment>? {
        TODO("Not yet implemented")
    }

    override suspend fun update(segment: WheelSegment) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteById(segmentId: Int) {
        TODO("Not yet implemented")
    }
}

@VisibleForTesting
suspend fun WheelSegmentRepository.addMultiple(segments: List<WheelSegment>) {
    segments.forEach { segment ->
        add(segment)
    }
}