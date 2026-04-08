package com.wonddak.coinaverage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wonddak.coinaverage.room.AppDatabase
import com.wonddak.coinaverage.util.Config

class CoinViewModelFactory(
    private val db: AppDatabase,
    private val config : Config
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CoinViewModel::class.java)) {
            return CoinViewModel(db,config) as T
        }
        throw IllegalArgumentException("Not found ViewModel class.")
    }
}