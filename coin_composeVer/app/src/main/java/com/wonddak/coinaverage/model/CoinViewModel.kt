package com.wonddak.coinaverage.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wonddak.coinaverage.repository.CoinRepository
import com.wonddak.coinaverage.room.CoinDetail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.withContext

class CoinViewModel(
    private val repository: CoinRepository
) : ViewModel() {

    private val _avg: MutableLiveData<Float> = MutableLiveData(0f)
    private val _total: MutableLiveData<Float> = MutableLiveData(0f)
    private val _count: MutableLiveData<Float> = MutableLiveData(0f)

    private val bgDispatcher = Dispatchers.IO

    var avg: LiveData<Float> = _avg
    var total: LiveData<Float> = _total
    var count: LiveData<Float> = _count

    private var title by mutableStateOf("")

    private val _coinDataList: MutableLiveData<List<CoinDetail>> = MutableLiveData()

    val coinDataList: LiveData<List<CoinDetail>>
        get() = _coinDataList


    fun getCoinData() {
        viewModelScope.launch(bgDispatcher) {
            withContext(bgDispatcher) {
                withContext(bgDispatcher) {
                    repository.getCoinData().let {
                        it.sortedBy { it.id }
                        _coinDataList.postValue(it)
                    }
                }
            }
        }
    }

    fun addNewCoinInfo() {
        viewModelScope.launch(bgDispatcher) {
            withContext(bgDispatcher) {
                repository.addNewCoinInfo()
            }
           updateInfo()
        }
    }

    fun updateInfo() {
        viewModelScope.launch(bgDispatcher) {
            withContext(bgDispatcher) {
                getCoinData()
            }
            var tSum = 0.0f
            var tCount = 0.0f
            var tAvg = 0.0f
            coinDataList.value?.forEach {
                tSum += (it.coinPrice * it.coinCount)
                tCount += it.coinCount
            }

            if (tCount != 0.0f) {
                tAvg = tSum / tCount
                _count.postValue(tCount)
                _total.postValue(tSum)
                _avg.postValue(tAvg)
            } else {
                _count.postValue(0.0f)
                _total.postValue(tSum)
                _avg.postValue(0.0f)
            }
        }
    }

    fun updateTitle() {
        title = repository.getTitle()
    }

    fun getTitles(): String {
        return title
    }

    fun updateCoinDetailCount(id: Int, count: Float) {
        viewModelScope.launch(bgDispatcher) {
            repository.updateCoinDetailCount(id, count)
            updateInfo()
        }
    }

    fun updateCoinDetailPrice(id: Int, price: Float) {
        viewModelScope.launch(bgDispatcher) {
            repository.updateCoinDetailPrice(id, price)
            updateInfo()
        }
    }

    fun deleteSelectItem(id: Int) {
        viewModelScope.launch(bgDispatcher) {
            repository.deleteCoinDetailById(id)
            updateInfo()
        }
    }

    fun clearCoinInfo() {
        viewModelScope.launch(bgDispatcher) {
            repository.clearCoinDetail()
            repository.addNewCoinInfo()
            repository.addNewCoinInfo()
            updateInfo()
        }
    }
}

