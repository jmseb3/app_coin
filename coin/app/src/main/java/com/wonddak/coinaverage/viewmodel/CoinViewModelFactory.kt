package com.wonddak.coinaverage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wonddak.coinaverage.room.AppDatabase

class CoinViewModelFactory(
    val db :AppDatabase
) :ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CoinViewModel::class.java)) {
            return CoinViewModel(db) as T
        }
        throw IllegalArgumentException("Not found ViewModel class.")
    }
}