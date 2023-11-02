package com.wonddak.coinaverage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wonddak.coinaverage.room.AppDatabase
import com.wonddak.coinaverage.room.CoinInfoAndCoinDetail
import com.wonddak.coinaverage.util.Config
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CoinViewModel(
    private val db: AppDatabase,
    private val config: Config
) : ViewModel() {

    private var _totalCoinList: MutableStateFlow<List<CoinInfoAndCoinDetail>> = MutableStateFlow(
        emptyList()
    )

    val totalCoinList: StateFlow<List<CoinInfoAndCoinDetail>>
        get() = _totalCoinList

    val id = config.getIdData().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = 1
    )

    val nowInfo: StateFlow<CoinInfoAndCoinDetail?> = totalCoinList.combine(id) { list, id ->
        list.find { it.coinInfo.coinId == id }
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

    fun setId(id:Int) {
        viewModelScope.launch {
            config.setId(id)
        }
    }

    fun setDec(dec:String) {
        viewModelScope.launch {
            config.setDec(dec)
        }
    }

    fun setNext(next:Boolean) {
        viewModelScope.launch {
            config.setNext(next)
        }
    }

}