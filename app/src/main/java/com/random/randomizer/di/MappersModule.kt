package com.random.randomizer.di

import com.random.randomizer.presentation.core.CoreMappers
import com.random.randomizer.presentation.core.CoreMappersImpl
import com.random.randomizer.presentation.screen.edit.EditMappers
import com.random.randomizer.presentation.screen.edit.EditMappersImpl
import com.random.randomizer.presentation.screen.edit.EditSegmentMappers
import com.random.randomizer.presentation.screen.edit.EditSegmentMappersImpl
import com.random.randomizer.presentation.screen.home.HomeMappers
import com.random.randomizer.presentation.screen.home.HomeMappersImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MappersModule {
    @Binds
    abstract fun bindCoreMappers(impl: CoreMappersImpl): CoreMappers

    @Binds
    abstract fun bindHomeMappers(impl: HomeMappersImpl): HomeMappers

    @Binds
    abstract fun bindEditMappers(impl: EditMappersImpl): EditMappers

    @Binds
    abstract fun bindEditSegmentMappers(impl: EditSegmentMappersImpl): EditSegmentMappers
}