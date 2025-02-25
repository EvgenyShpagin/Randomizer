package com.random.randomizer.data.source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WheelSegmentDao {

    @Query("SELECT * FROM wheel_segment WHERE id = :id")
    fun observeById(id: Int): Flow<WheelSegment?>

    @Query("SELECT * FROM wheel_segment")
    fun observeAll(): Flow<List<WheelSegment>>

    @Query("SELECT * FROM wheel_segment WHERE id = :id")
    suspend fun getById(id: Int): WheelSegment?

    @Query("SELECT * FROM wheel_segment")
    suspend fun getAll(): List<WheelSegment>

    @Insert
    suspend fun insert(wheelSegment: WheelSegment): Long

    @Update
    suspend fun update(wheelSegment: WheelSegment)

    @Query("DELETE FROM wheel_segment WHERE id = :id")
    suspend fun deleteById(id: Int)

}