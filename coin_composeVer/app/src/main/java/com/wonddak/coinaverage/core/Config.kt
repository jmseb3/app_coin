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
                is Float -> putFloat(key, data)
                is Long -> putLong(key, data)
            }
            apply()
        }

    }

    fun getInt(key: String): Int {
        return prefs.getInt(key, 0)
    }

    fun getBoolean(key: String): Boolean {
        return prefs.getBoolean(key, false)
    }

    fun getString(key: String): String? {
        return prefs.getString(key, null)
    }

    fun getLong(key:String):Long {
        return prefs.getLong(key,0L)
    }

    fun getFloat(key:String):Float {
        return prefs.getFloat(key,0f)
    }

}