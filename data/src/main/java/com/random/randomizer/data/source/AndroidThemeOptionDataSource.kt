package com.random.randomizer.data.source

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.random.randomizer.data.model.AndroidThemeOption
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class AndroidThemeOptionDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val COLOR_SCHEME = intPreferencesKey("color_scheme")
    private val DARK_MODE_CONFIG = intPreferencesKey("dark_mode_config")

    suspend fun setThemeOption(themeOption: AndroidThemeOption) {
        context.dataStore.edit { preferences ->
            when (themeOption) {
                is AndroidThemeOption.ColorScheme ->
                    preferences[COLOR_SCHEME] = themeOption.ordinal

                is AndroidThemeOption.DarkModeConfig ->
                    preferences[DARK_MODE_CONFIG] = themeOption.ordinal
            }
        }
    }

    fun getThemeOptionsStream(): Flow<List<AndroidThemeOption>> {
        return context.dataStore.data.map { preferences ->
            val colorScheme = preferences[COLOR_SCHEME]?.let { ordinal ->
                AndroidThemeOption.ColorScheme.entries[ordinal]
            } ?: DefaultColorScheme

            val darkModeConfig = preferences[DARK_MODE_CONFIG]?.let { ordinal ->
                AndroidThemeOption.DarkModeConfig.entries[ordinal]
            } ?: DefaultDarkModeConfig

            listOf(colorScheme, darkModeConfig)
        }
    }

    private companion object {
        val DefaultColorScheme = AndroidThemeOption.ColorScheme.Static
        val DefaultDarkModeConfig = AndroidThemeOption.DarkModeConfig.System
    }
}