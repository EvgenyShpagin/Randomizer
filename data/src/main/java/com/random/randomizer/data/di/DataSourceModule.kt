package com.random.randomizer.data.di

import com.random.randomizer.data.source.WheelSegmentDataSource
import com.random.randomizer.data.source.WheelSegmentDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun bindWheelSegmentDataSource(
        impl: WheelSegmentDataSourceImpl
    ): WheelSegmentDataSource

}