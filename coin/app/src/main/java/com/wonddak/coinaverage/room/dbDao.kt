package com.wonddak.coinaverage.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface dbDao {

    // 코인정보 관련
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCoinInfoData(coinInfo: CoinInfo) : Long

    @Delete
    fun deleteCoinInfoData(coinInfo: CoinInfo)

    @Query("SELECT * FROM CoinInfo WHERE coinId=:coinId")
    fun getCoinInfoDataById(coinId: Int): CoinInfo

    @Query("SELECT coinName FROM CoinInfo WHERE coinId=:coinId")
    fun getCoinInfoNameById(coinId: Int): String

    @Query("SELECT coinId FROM CoinInfo WHERE coinName=:coinName")
    fun getCoinInfoIdByName(coinName: String): Int

    @Query("SELECT coinName FROM CoinInfo")
    fun getCoinInfoNameList(): List<String>

    @Query("SELECT * FROM CoinInfo ")
    fun getCoinInfoLiveData(): LiveData<List<CoinInfo>>

    @Query("UPDATE coininfo SET coinName=:coinName WHERE coinId=:coinId")
    fun updateCoinInfoName(coinId: Int, coinName: String)

    @Query("DELETE FROM coininfo WHERE coinId =:id")
    fun deleteCoinInfolById(id: Int)

//    코인디테일관련련

    @Query("SELECT * FROM coindetail WHERE coinId=:coinId")
    fun getCoinDetailById(coinId: Int): LiveData<List<CoinDetail>>

    @Query("SELECT * FROM coindetail WHERE coinId=:coinId")
    fun getCoinDetailByIdNotLive(coinId: Int): List<CoinDetail>

    @Query("SELECT coinPrice FROM coindetail WHERE coinId=:coinId")
    fun getCoinDetailPriceById(coinId: Int): List<Float>

    @Query("SELECT coinCount FROM coindetail WHERE coinId=:coinId")
    fun getCoinDetailCountById(coinId: Int): List<Float>


    @Query("SELECT id FROM coindetail WHERE coinId=:coinId")
    fun getCoinDetailId(coinId: Int): List<Int>

    @Query("DELETE FROM coindetail WHERE id =:id")
    fun deleteCoinDetailById(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCoinDetailData(coinDetail: CoinDetail)

    @Query("UPDATE coindetail SET coinCount= :coinCount,coinPrice= :coinPrice  WHERE coinId=:coinId")
    fun updateCoinDetailByCoinInfoId(coinId: Int, coinCount: Float, coinPrice: Float)

    @Query("UPDATE coindetail SET coinPrice= :coinPrice,coinCount= :coinCount  WHERE id=:id")
    fun updateCoinDetailByCoinitemId(id: Int, coinPrice: Float, coinCount: Float)

    @Query("UPDATE coindetail SET coinCount= :coinCount  WHERE id=:id")
    fun updateCoinDetailByCoinitemIdCount(id: Int, coinCount: Float)

    @Query("UPDATE coindetail SET coinPrice= :coinPrice  WHERE id=:id")
    fun updateCoinDetailByCoinitemIdPrice(id: Int, coinPrice: Float)

    @Query("DELETE From coindetail WHERE coinId=:coinId")
    fun clearCoinDetail(coinId: Int)

    @Transaction
    @Query("SELECT * FROM coininfo")
    fun getCoinInfoAndCoinDetail(): List<CoinInfoAndCoinDetail>
}