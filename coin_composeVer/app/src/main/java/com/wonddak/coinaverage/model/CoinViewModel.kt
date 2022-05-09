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
    var avg by mutableStateOf(0f)
    var total by mutableStateOf(0f)
    var count by mutableStateOf(0f)

    private var title by  mutableStateOf("")

    private val _coinDataList: MutableLiveData<List<CoinDetail>> = MutableLiveData()

    val coinDataList: LiveData<List<CoinDetail>> = _coinDataList

    fun getCoinData() {
        repository.getCoinData().let {
            _coinDataList.postValue(it)
        }
    }

    fun addNewCoinInfo(){
        repository.addNewCoinInfo()
        getCoinData()
    }

    fun updateTitle(){
        title = repository.getTitle()
    }

    fun getTitles():String{
        return title
    }
}

