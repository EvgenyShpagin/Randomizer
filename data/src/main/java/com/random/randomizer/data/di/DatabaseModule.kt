package com.random.randomizer.data.di

import android.content.Context
import androidx.room.Room
import com.random.randomizer.data.source.WheelSegmentDao
import com.random.randomizer.data.source.WheelSegmentDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): WheelSegmentDatabase {
        return Room.databaseBuilder(
            context,
            WheelSegmentDatabase::class.java,
            "WheelSegments.db"
        ).build()
    }

    @Provides
    fun provideWheelSegmentDao(database: WheelSegmentDatabase): WheelSegmentDao {
        return database.wheelSegmentDao
    }
}