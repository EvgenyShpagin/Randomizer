package com.random.randomizer.data.repository

import com.random.randomizer.data.di.ApplicationScope
import com.random.randomizer.data.di.IoDispatcher
import com.random.randomizer.data.model.AndroidThemeOption
import com.random.randomizer.data.source.AndroidThemeOptionDataSource
import com.random.randomizer.domain.model.ThemeOption
import com.random.randomizer.domain.repository.ThemeOptionRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AndroidThemeOptionRepository @Inject constructor(
    private val dataSource: AndroidThemeOptionDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @ApplicationScope private val externalScope: CoroutineScope
) : ThemeOptionRepository {

    override suspend fun setThemeOption(themeOption: ThemeOption) {
        withContext(ioDispatcher) {
            externalScope.async {
                dataSource.setThemeOption(themeOption as AndroidThemeOption)
            }.await()
        }
    }

    override fun getThemeOptionsStream(): Flow<List<ThemeOption>> {
        return dataSource.getThemeOptionsStream()
    }
}