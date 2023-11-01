package com.wonddak.coinaverage.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wonddak.coinaverage.room.AppDatabase
import com.wonddak.coinaverage.room.CoinInfoAndCoinDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CoinViewModel(
    val db: AppDatabase
) : ViewModel() {

    private var _totalCoinList: MutableStateFlow<List<CoinInfoAndCoinDetail>> = MutableStateFlow(
        emptyList()
    )

    val totalCoinList: StateFlow<List<CoinInfoAndCoinDetail>>
        get() = _totalCoinList

    init {
        viewModelScope.launch {
            db.dbDao().getAll().collect {
                _totalCoinList.value = it
            }
        }
    }

}