package com.wonddak.coinaverage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wonddak.coinaverage.room.AppDatabase
import com.wonddak.coinaverage.room.CoinInfoAndCoinDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CoinViewModel(
    val db: AppDatabase
) : ViewModel() {

    private var _totalCoinList: MutableStateFlow<List<CoinInfoAndCoinDetail>> = MutableStateFlow(
        emptyList()
    )

    val totalCoinList: StateFlow<List<CoinInfoAndCoinDetail>>
        get() = _totalCoinList

    private var _idData: MutableStateFlow<Int> = MutableStateFlow(1)

    val idData: StateFlow<Int>
        get() = _idData

    val nowInfo: StateFlow<CoinInfoAndCoinDetail?> = totalCoinList.combine(idData) { a, b ->
        a.find { it.coinInfo.coinId == b }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = null
    )

    init {
        viewModelScope.launch {
            db.dbDao().getAllFlow().collect {
                _totalCoinList.value = it
            }
        }
    }

}