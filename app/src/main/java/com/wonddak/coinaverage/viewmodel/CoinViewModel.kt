package com.wonddak.coinaverage.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wonddak.coinaverage.room.AppDatabase
import com.wonddak.coinaverage.room.CoinDetail
import com.wonddak.coinaverage.room.CoinInfo
import com.wonddak.coinaverage.room.CoinInfoAndCoinDetail
import com.wonddak.coinaverage.util.Config
import kotlinx.coroutines.Dispatchers
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
        initialValue = -1
    )

    val next = config.getNext().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = false
    )

    val dec = config.getDec().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = "#,###.00"
    )
    val reward = config.getReward().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = 0L
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
        viewModelScope.launch {
            reward.collect {
                Log.d("JWH", "get reward : $it")
            }
        }
    }

    fun setId(id: Int) {
        viewModelScope.launch {
            config.setId(id)
        }
    }

    fun setDec(dec: String) {
        viewModelScope.launch {
            config.setDec(dec)
        }
    }

    fun setNext(next: Boolean) {
        viewModelScope.launch {
            config.setNext(next)
        }
    }

    fun setReward(time:Long) {
        viewModelScope.launch {
            config.setReward(time)
        }
    }

    fun insertCoin(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val id = db.dbDao().insertCoinInfoData(CoinInfo(null, name)).toInt()
            repeat(2) {
                db.dbDao().insertCoinDetailData(CoinDetail(null, id))
            }
            setId(id)
        }
    }

    fun updateCoinName(coinId: Int, name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            db.dbDao().updateCoinInfoName(coinId, name)
        }
    }

    fun deleteCoin(coinId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            db.dbDao().deleteCoinInfoById(coinId)
        }
    }

    fun addDetail(coinId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            db.dbDao().insertCoinDetailData(CoinDetail(null, coinId))
        }
    }

    fun resetCoin(coinId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            db.dbDao().clearCoinDetail(coinId)
            repeat(2) {
                db.dbDao().insertCoinDetailData(CoinDetail(null, coinId))
            }
        }
    }

    fun updateCoinDetailPrice(
        coinDetailId: Int,
        coinPrice: Float,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            db.dbDao()
                .updateCoinDetailPrice(coinDetailId, coinPrice)
        }
    }

    fun updateCoinDetailCount(
        coinDetailId: Int,
        coinCount: Float,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            db.dbDao()
                .updateCoinDetailCount(coinDetailId, coinCount)
        }
    }

    fun updateCoinDetail(
        coinDetailId: Int,
        coinPrice: Float,
        coinCount: Float
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            db.dbDao()
                .updateCoinDetail(coinDetailId, coinPrice, coinCount)
        }
    }

    fun resetCoinDetail(
        coinDetailId: Int,
    ) = updateCoinDetail(coinDetailId, 0.0f, 0.0f)

    fun deleteCoinDetail(
        coinDetailId: Int
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            db.dbDao().deleteCoinDetailById(coinDetailId)
        }
    }

    var showUpdateNeed by mutableStateOf(false)
}