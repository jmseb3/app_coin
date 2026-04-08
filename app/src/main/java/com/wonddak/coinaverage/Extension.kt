package com.wonddak.coinaverage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import com.wonddak.coinaverage.room.CoinDetail
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

fun CoinInfoAndCoinDetail.getAvg(): Float {
    return getTotalPriceSum() / getAllCount()
}

fun CoinInfoAndCoinDetail.getCoinId(): Int {
    return this.coinInfo.coinId!!
}

fun CoinDetail.getTotalPrice(): Float {
    return this.coinPrice * this.coinCount
}

fun CoinDetail.getPriceString() :String {
    return if (coinPrice == 0.0f) {
        ""
    } else {
        coinPrice.toString()
    }
}

fun CoinDetail.getCountString() :String {
    return if (coinCount == 0.0f) {
        ""
    } else {
        coinCount.toString()
    }
}


fun Float.toFormat(
    dec: String,
    prefix: String = ""
): String {
    return DecimalFormat(dec).format(this) + prefix
}

fun Float.toFormat(
    format: Const.DecFormat,
    prefix: String = ""
) = this.toFormat(format.dec, prefix)


inline fun Modifier.noRippleClickable(
    crossinline onClick: () -> Unit = {},
): Modifier = composed {
    this.clickable(indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }

}