package com.example.projectone.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "theme_setting")

class SettingPreferences private constructor(private val dataStore: DataStore<Preferences>){

    private val SWITCH_THEME_KEY = booleanPreferencesKey("switch_theme")

    fun getThemeConfig(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[SWITCH_THEME_KEY] ?: false
        }
    }

    suspend fun saveThemeConfig(isDarkMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[SWITCH_THEME_KEY] = isDarkMode
        }
    }

    companion object {
        @Volatile
        private var instansiasi : SettingPreferences? = null

        fun getInstansiasi(dataStore: DataStore<Preferences>): SettingPreferences {
            return instansiasi ?: synchronized(this) {
                val instance = SettingPreferences(dataStore)
                instansiasi = instance
                instance
            }
        }

    }
}