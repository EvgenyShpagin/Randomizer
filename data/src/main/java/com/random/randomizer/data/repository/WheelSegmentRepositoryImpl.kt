package com.random.randomizer.data.repository

import com.random.randomizer.domain.model.WheelSegment
import com.random.randomizer.domain.repository.WheelSegmentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WheelSegmentRepositoryImpl @Inject constructor() : WheelSegmentRepository {
    override suspend fun add(segment: WheelSegment): Int {
        TODO("Not yet implemented")
    }

    override suspend fun getAll(): List<WheelSegment> {
        TODO("Not yet implemented")
    }

    override fun getAllStream(): Flow<List<WheelSegment>> {
        TODO("Not yet implemented")
    }

    override suspend fun get(segmentId: Int): WheelSegment? {
        TODO("Not yet implemented")
    }

    override fun getStream(segmentId: Int): Flow<WheelSegment?> {
        TODO("Not yet implemented")
    }

    override suspend fun update(segment: WheelSegment) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteById(segmentId: Int) {
        TODO("Not yet implemented")
    }
}