package com.random.randomizer.di

import com.random.randomizer.data.repository.WheelSegmentRepositoryImpl
import com.random.randomizer.domain.repository.WheelSegmentRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindWheelSegmentRepository(
        impl: WheelSegmentRepositoryImpl
    ): WheelSegmentRepository

}