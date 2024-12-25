package com.random.randomizer.domain.repository

import com.random.randomizer.domain.model.WheelSegment
import kotlinx.coroutines.flow.Flow

interface WheelSegmentRepository {
    suspend fun add(segment: WheelSegment)
    suspend fun getAll(): List<WheelSegment>
    fun getAllStream(): Flow<List<WheelSegment>>
    suspend fun get(segmentId: Int): WheelSegment?
    fun getStream(segmentId: Int): Flow<WheelSegment>
    suspend fun update(segment: WheelSegment)
    suspend fun deleteById(segmentId: Int)
}