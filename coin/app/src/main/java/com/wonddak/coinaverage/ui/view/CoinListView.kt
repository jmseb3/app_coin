package com.wonddak.coinaverage.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wonddak.coinaverage.room.CoinDetail
import com.wonddak.coinaverage.room.CoinInfo
import com.wonddak.coinaverage.room.CoinInfoAndCoinDetail
import com.wonddak.coinaverage.ui.theme.MATCH1

@Composable
fun CoinListView() {
}

@Composable
private fun CoinListItem(
    item: CoinInfoAndCoinDetail
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MATCH1
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(
                text = item.coinInfo.coinName,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Row {
                Text(text = "총 매수 금액")
            }
            Row {
                Text(text = "총 매수 량")
            }
        }
    }

}

@Composable
@Preview
private fun itemPreview() {
    CoinListItem(
        item = CoinInfoAndCoinDetail(
            CoinInfo(null, "메인코인"),
            listOf(CoinDetail(null, 1, 0.5f, 0.5f))
        )
    )
}