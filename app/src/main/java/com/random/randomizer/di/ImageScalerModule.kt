package com.random.randomizer.di

import com.random.randomizer.data.util.ImageScalerImpl
import com.random.randomizer.domain.util.ImageScaler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ImageScalerModule {
    @Binds
    abstract fun bindImageScaler(impl: ImageScalerImpl): ImageScaler
}