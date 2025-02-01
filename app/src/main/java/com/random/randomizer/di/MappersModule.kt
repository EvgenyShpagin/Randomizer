package com.random.randomizer.di

import com.random.randomizer.presentation.core.CoreMappers
import com.random.randomizer.presentation.core.CoreMappersImpl
import com.random.randomizer.presentation.screen.edit.EditMappers
import com.random.randomizer.presentation.screen.edit.EditMappersImpl
import com.random.randomizer.presentation.screen.segment.WheelSegmentMappers
import com.random.randomizer.presentation.screen.segment.WheelSegmentMappersImpl
import com.random.randomizer.presentation.screen.home.HomeMappers
import com.random.randomizer.presentation.screen.home.HomeMappersImpl
import com.random.randomizer.presentation.screen.results.ResultsMappers
import com.random.randomizer.presentation.screen.results.ResultsMappersImpl
import com.random.randomizer.presentation.screen.spin.SpinMappers
import com.random.randomizer.presentation.screen.spin.SpinMappersImpl
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
    abstract fun bindEditSegmentMappers(impl: WheelSegmentMappersImpl): WheelSegmentMappers

    @Binds
    abstract fun bindSpinMappers(impl: SpinMappersImpl): SpinMappers

    @Binds
    abstract fun bindResultsMappers(impl: ResultsMappersImpl): ResultsMappers
}