package com.wonddak.coinaverage

import com.wonddak.coinaverage.room.CoinInfoAndCoinDetail
import java.text.DecimalFormat

fun CoinInfoAndCoinDetail.getTotalPriceSum(): Float {
    return this.coinDetailList.fold(0f) { acc, coinDetail ->
        acc + coinDetail.coinPrice * coinDetail.coinCount
    }
}

fun CoinInfoAndCoinDetail.getAllCount(): Float {
    return this.coinDetailList.fold(0f) { acc, coinDetail ->
        acc + coinDetail.coinCount
    }
}

fun CoinInfoAndCoinDetail.getCoinId(): Int {
    return this.coinInfo.coinId!!
}


fun Float.toFormat(
    dec: String,
    prefix: String = ""
): String {
    val dec = DecimalFormat(dec)
    return dec.format(this) + prefix
}

fun Float.toFormat(
    format: Const.DecFormat,
    prefix: String = ""
) = this.toFormat(format.dec, prefix)