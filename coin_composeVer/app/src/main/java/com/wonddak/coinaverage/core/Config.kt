package com.wonddak.coinaverage.core

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class Config(context: Context) {
    private val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun setConfig(key: String, data: Any) {
        prefs.edit().apply {
            when (data) {
                is Int -> putInt(key, data)
                is Boolean -> putBoolean(key, data)
                is String -> putString(key, data)
                else -> {}
            }
            commit()
        }

    }

    fun getIntConfig(key: String): Int {
        return prefs.getInt(key, 0)
    }

    fun getBooleanConfig(key: String): Boolean {
        return prefs.getBoolean(key, false)
    }

    fun getStringConfig(key: String): String? {
        return prefs.getString(key, null)
    }

}