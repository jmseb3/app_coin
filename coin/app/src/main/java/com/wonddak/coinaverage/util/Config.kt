package com.wonddak.coinaverage.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.preference.Preference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class Config(context: Context) {
    companion object {
        private const val PrefName = "coindata"

        private var instance: Config? = null
        fun getInstance(context: Context): Config {
            if (instance == null) {
                instance = Config(context)
            }
            return instance!!
        }

        object Key {
            const val Next = "next"
            const val Dec = "dec"
            const val IdData = "iddata"

            val NextKey = booleanPreferencesKey(Next)
            val DecKey = stringPreferencesKey(Dec)
            val IdKey = intPreferencesKey(IdData)
        }
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = PrefName,
        produceMigrations = { context ->
            listOf(
                SharedPreferencesMigration(
                    context,
                    PrefName,
                    setOf(Key.Next, Key.Dec, Key.IdData)
                )
            )
        }
    )

    private val dataStore = context.dataStore

    fun getIdData(): Flow<Int> {
        return dataStore.data
            .map { preferences ->
                // No type safety.
                preferences[Key.IdKey] ?: 0
            }
    }

    fun getDec(): Flow<String> {
        return dataStore.data
            .map { preferences ->
                // No type safety.
                preferences[Key.DecKey] ?: "#,###.00"
            }
    }

    fun getNext(): Flow<Boolean> {
        return dataStore.data
            .map { preferences ->
                // No type safety.
                preferences[Key.NextKey] ?: false
            }
    }

    suspend fun setId(id: Int) {
        dataStore.edit { settings ->
            settings[Key.IdKey] = id
        }
    }

    suspend fun setDec(dec: String) {
        dataStore.edit { settings ->
            settings[Key.DecKey] = dec
        }
    }

    suspend fun setNext(next: Boolean) {
        dataStore.edit { settings ->
            settings[Key.NextKey] = next
        }
    }
}