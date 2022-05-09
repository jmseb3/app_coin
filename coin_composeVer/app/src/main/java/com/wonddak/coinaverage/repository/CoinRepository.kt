package com.wonddak.coinaverage.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.wonddak.coinaverage.R
import com.wonddak.coinaverage.core.Config
import com.wonddak.coinaverage.core.Const
import com.wonddak.coinaverage.room.AppDatabase
import com.wonddak.coinaverage.room.CoinDetail
import com.wonddak.coinaverage.room.dbDao
import java.text.DecimalFormat

class CoinRepository(val mContext:Context) {
    private val mConfig = Config(mContext)
    private val format = mConfig.getString(Const.DECIMAL_FORMAT) ?: Const.DecimalFormat.TWO.value
    val dec = DecimalFormat(format)
    private var idData = mConfig.getInt(Const.ID_DATA, 1)
    private val db = AppDatabase.getInstance(mContext)
    init {

    }

    fun getTitle():String{
        return mContext.getString(R.string.app_name)
    }

    fun getCoinData():List<CoinDetail>{
        return db.dbDao().getCoinDetailByIdNotLive(idData)
    }

    fun addNewCoinInfo(){
        db.dbDao().insertCoinDetailData(CoinDetail(null,idData))
    }
}