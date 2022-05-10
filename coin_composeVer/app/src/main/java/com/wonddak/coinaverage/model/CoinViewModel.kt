package com.wonddak.coinaverage.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wonddak.coinaverage.repository.CoinRepository
import com.wonddak.coinaverage.room.CoinDetail

class CoinViewModel(
    private val repository: CoinRepository
) :ViewModel() {

    private val _avg:MutableLiveData<Float> = MutableLiveData(0f)
    private val _total:MutableLiveData<Float> = MutableLiveData(0f)
    private val _count:MutableLiveData<Float> = MutableLiveData(0f)

    var avg:LiveData<Float> = _avg
    var total :LiveData<Float> = _total
    var count :LiveData<Float> = _count

    private var title by  mutableStateOf("")

    private val _coinDataList: MutableLiveData<List<CoinDetail>> = MutableLiveData()

    val coinDataList: LiveData<List<CoinDetail>> = _coinDataList

    fun getCoinData() {
        repository.getCoinData().let {
            _coinDataList.postValue(it)
        }
        updateInfo()
    }

    fun addNewCoinInfo(){
        repository.addNewCoinInfo()
        getCoinData()
    }

    private fun updateInfo(){
        var tSum = 0.0f
        var tCount = 0.0f
        var tAvg =0.0f
        coinDataList.value?.forEach {
            tSum += (it.coinPrice*it.coinCount)
            tCount +=it.coinCount
        }

        if (tCount != 0.0f) {
            tAvg = tSum / tCount
            _count.postValue(tCount)
            _total.postValue(tSum)
            _avg.postValue(tAvg)
        }else{
            _count.postValue(0.0f)
            _total.postValue(tSum)
            _avg.postValue(0.0f)
        }
    }

    fun updateTitle(){
        title = repository.getTitle()
    }

    fun getTitles():String{
        return title
    }
    fun updateCoinDetailCount(id: Int, count: Float) {
        repository.updateCoinDetailCount(id, count)
    }

    fun updateCoinDetailPrice(id: Int, price: Float) {
        repository.updateCoinDetailPrice(id, price)
    }
}

