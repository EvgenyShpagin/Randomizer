package com.random.randomizer.domain.repository

import com.random.randomizer.domain.model.ThemeOption
import kotlinx.coroutines.flow.Flow

interface ThemeOptionRepository {
    suspend fun setThemeOption(themeOption: ThemeOption)
    fun getThemeOptionsStream(): Flow<List<ThemeOption>>
}