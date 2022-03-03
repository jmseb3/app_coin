package com.wonddak.coinaverage.API

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

data class upbitList(
    val english_name: String,
    val korean_name: String,
    val market: String
)

data class infolist(
    val acc_trade_price: Double,
    val acc_trade_price_24h: Double,
    val acc_trade_volume: Double,
    val acc_trade_volume_24h: Double,
    val change: String,
    val change_price: Double,
    val change_rate: Double,
    val high_price: Double,
    val highest_52_week_date: String,
    val highest_52_week_price: Double,
    val low_price: Double,
    val lowest_52_week_date: String,
    val lowest_52_week_price: Double,
    val market: String,
    val opening_price: Double,
    val prev_closing_price: Double,
    val signed_change_price: Double,
    val signed_change_rate: Double,
    val timestamp: Long,
    val trade_date: String,
    val trade_date_kst: String,
    val trade_price: Double,
    val trade_time: String,
    val trade_time_kst: String,
    val trade_timestamp: Long,
    val trade_volume: Double
)

class upbitArr : ArrayList<upbitList>()
class DetailArr : ArrayList<infolist>()

interface upbitAPI {
    @GET("market/all/")
    fun getlist(): Call<upbitArr>

    @GET("ticker?")
    fun getinfo(
        @Query("markets") path: String?
    ): Call<DetailArr>
}

object upBitClient {
    var api: upbitAPI

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.upbit.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(upbitAPI::class.java)
    }
}


