package com.app.paytrack.utlis

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


object ThemePreference {
    private val Context.dataStore by preferencesDataStore(name = "settings")

    private val DARK_MODE = booleanPreferencesKey("dark_mode")

    suspend fun setDarkMode(context: Context, isDarkMode: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DARK_MODE] = isDarkMode
        }
    }

    fun getDarkModeFlow(context: Context): Flow<Boolean> {
        return context.dataStore.data
            .map { preferences -> preferences[DARK_MODE] ?: false }
    }
}
