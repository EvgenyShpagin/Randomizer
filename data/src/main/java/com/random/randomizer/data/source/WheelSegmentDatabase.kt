package com.random.randomizer.data.source

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WheelSegment::class], version = 1, exportSchema = false)
abstract class WheelSegmentDatabase : RoomDatabase() {
    abstract val wheelSegmentDao: WheelSegmentDao
}