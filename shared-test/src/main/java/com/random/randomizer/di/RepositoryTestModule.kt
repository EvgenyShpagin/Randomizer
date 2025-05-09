package com.random.randomizer.di

import com.random.randomizer.data.repository.FakeAndroidThemeOptionRepository
import com.random.randomizer.data.repository.FakeWheelSegmentRepository
import com.random.randomizer.domain.repository.ThemeOptionRepository
import com.random.randomizer.domain.repository.WheelSegmentRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
abstract class RepositoryTestModule {

    @Singleton
    @Binds
    abstract fun bindWheelSegmentRepository(
        impl: FakeWheelSegmentRepository
    ): WheelSegmentRepository

    @Singleton
    @Binds
    abstract fun bindThemeOptionRepository(
        impl: FakeAndroidThemeOptionRepository
    ): ThemeOptionRepository
}