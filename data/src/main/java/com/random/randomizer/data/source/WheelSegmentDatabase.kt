package com.random.randomizer.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [WheelSegment::class], version = 1, exportSchema = false)
@TypeConverters(ImageConverter::class)
abstract class WheelSegmentDatabase : RoomDatabase() {
    abstract val wheelSegmentDao: WheelSegmentDao
}