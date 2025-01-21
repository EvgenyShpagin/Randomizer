package com.random.randomizer.data.source

import kotlinx.coroutines.flow.Flow

interface WheelSegmentDataSource {
    suspend fun getById(id: Int): WheelSegment?
    suspend fun insert(wheelSegment: WheelSegment): Long
    suspend fun update(wheelSegment: WheelSegment)
    suspend fun getAll(): List<WheelSegment>
    suspend fun deleteById(id: Int)
    fun observeById(id: Int): Flow<WheelSegment>
    fun observeAll(): Flow<List<WheelSegment>>
}