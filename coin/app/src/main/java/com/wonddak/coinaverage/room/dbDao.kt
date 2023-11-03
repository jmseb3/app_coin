package com.wonddak.coinaverage.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface dbDao {

    @Transaction
    @Query("SELECT * FROM CoinInfo")
    fun getAllFlow(): Flow<List<CoinInfoAndCoinDetail>>

    @Transaction
    @Query("SELECT * FROM CoinInfo")
    fun getAll(): List<CoinInfoAndCoinDetail>

    // 코인정보 관련
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCoinInfoData(coinInfo: CoinInfo): Long

    @Query("UPDATE CoinInfo SET coinName=:coinName WHERE coinId=:coinId")
    fun updateCoinInfoName(coinId: Int, coinName: String)

    @Query("DELETE FROM CoinInfo WHERE coinId =:id")
    fun deleteCoinInfoById(id: Int)

//    코인디테일관련련

    @Query("DELETE FROM CoinDetail WHERE id =:id")
    fun deleteCoinDetailById(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCoinDetailData(coinDetail: CoinDetail)

    @Query("UPDATE CoinDetail SET coinPrice= :coinPrice,coinCount= :coinCount  WHERE id=:id")
    fun updateCoinDetail(id: Int, coinPrice: Float, coinCount: Float)

    @Query("UPDATE CoinDetail SET coinCount= :coinCount  WHERE id=:id")
    fun updateCoinDetailCount(id: Int, coinCount: Float)

    @Query("UPDATE CoinDetail SET coinPrice= :coinPrice  WHERE id=:id")
    fun updateCoinDetailPrice(id: Int, coinPrice: Float)

    @Query("DELETE From CoinDetail WHERE coinId=:coinId")
    fun clearCoinDetail(coinId: Int)
}