package com.wonddak.coinaverage.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class Config(context: Context) {

    val prefs: SharedPreferences = context.getSharedPreferences("coindata", Context.MODE_PRIVATE)

    companion object {
        private var instance :Config? = null
        fun getInstance(context: Context) : Config {
            if (instance == null) {
                instance = Config(context)
            }
            return instance!!
        }

        object Key {
            const val Next ="next"
            const val Dec = "dec"
            const val IdData = "iddata"
        }
    }

    fun getIdData() : Int {
        return prefs.getInt(Key.IdData,1)
    }

    fun getDec() :String? {
        return prefs.getString(Key.Dec, "#,###.00")
    }

    fun getNext() :Boolean {
        return prefs.getBoolean(Key.Next,false)
    }

    fun setId(id:Int) {
        prefs.edit {
            putInt(Key.IdData,id)
            commit()
        }
    }

    fun setDec(dec:String) {
        prefs.edit {
            putString(Key.Dec,dec)
            commit()
        }
    }

    fun setNext(next:Boolean) {
        prefs.edit {
            putBoolean(Key.Next,next)
            commit()
        }
    }
}