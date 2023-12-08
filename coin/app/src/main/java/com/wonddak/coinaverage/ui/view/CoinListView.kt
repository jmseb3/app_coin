package com.wonddak.coinaverage.ui.view

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wonddak.coinaverage.Const
import com.wonddak.coinaverage.R
import com.wonddak.coinaverage.getAllCount
import com.wonddak.coinaverage.getCoinId
import com.wonddak.coinaverage.getTotalPriceSum
import com.wonddak.coinaverage.room.CoinDetail
import com.wonddak.coinaverage.room.CoinInfo
import com.wonddak.coinaverage.room.CoinInfoAndCoinDetail
import com.wonddak.coinaverage.toFormat
import com.wonddak.coinaverage.ui.common.CommonText
import com.wonddak.coinaverage.ui.dialog.DeleteDialog
import com.wonddak.coinaverage.ui.dialog.NameDialog
import com.wonddak.coinaverage.ui.theme.MATCH1
import com.wonddak.coinaverage.ui.theme.MATCH2
import com.wonddak.coinaverage.viewmodel.CoinViewModel

@Composable
fun CoinListView(
    viewModel: CoinViewModel,
    moveToMain:() -> Unit
) {
    val dec by viewModel.dec.collectAsState()
    val totalCoinList by viewModel.totalCoinList.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize().background(MATCH2)
    ) {
        CommonText(
            text = "길게 눌러서 이름 수정이 가능합니다.",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(totalCoinList.size) {
                val item = totalCoinList[it]
                CoinListItem(
                    item = item,
                    dec = dec,
                    clickItem = {
                        viewModel.setId(item.getCoinId())
                        moveToMain()
                    },
                    clickLongItem = { name ->
                        viewModel.updateCoinName(item.getCoinId(), name)
                    },
                    deleteRequest = {
                        if (totalCoinList.size <= 1) {
                            Toast.makeText(context, "최소 하나는 존재해야 합니다.", Toast.LENGTH_SHORT).show()
                            return@CoinListItem false
                        } else {
                            return@CoinListItem true
                        }
                    },
                    deleteItem = {
                        viewModel.deleteCoin(item.getCoinId())
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CoinListItem(
    item: CoinInfoAndCoinDetail,
    dec: String,
    clickItem: () -> Unit,
    clickLongItem: (name: String) -> Unit,
    deleteRequest: () -> Boolean,
    deleteItem: () -> Unit
) {
    var showDeleteDialog by remember {
        mutableStateOf(false)
    }
    var showNameEditDialog by remember {
        mutableStateOf(false)
    }
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MATCH1
        ),
        modifier = Modifier.combinedClickable(
            onClick = {
                clickItem()
            },
            onLongClick = {
                showNameEditDialog = true
            }
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                CommonText(
                    text = item.coinInfo.coinName,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = MATCH2
                )
                Row {
                    CommonText(
                        text = "총 매수 금액",
                        modifier = Modifier.weight(1f),
                        color = MATCH2
                    )
                    CommonText(
                        text = item.getTotalPriceSum().toFormat(dec, "원"),
                        modifier = Modifier.weight(3f),
                        color = MATCH2
                    )
                }
                Row {
                    CommonText(
                        text = "총 매수 량",
                        modifier = Modifier.weight(1f),
                        color = MATCH2
                    )
                    CommonText(
                        text = item.getAllCount().toFormat(dec, "개"),
                        modifier = Modifier.weight(3f),
                        color = MATCH2
                    )
                }
            }
            IconButton(
                modifier = Modifier.align(Alignment.TopEnd),
                onClick = {
                    showDeleteDialog = deleteRequest()
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_clear_24),
                    contentDescription = null
                )
            }
        }
    }

    if (showDeleteDialog) {
        DeleteDialog(onDismissRequest = { showDeleteDialog = false }) {
            deleteItem()
        }
    }

    if (showNameEditDialog) {
        NameDialog(
            coinInfoAndCoinDetail = item,
            onDismissRequest = { showNameEditDialog = false }
        ) { name ->
            clickLongItem(name)
        }
    }

}

@Composable
@Preview
private fun itemPreview() {
    CoinListItem(
        item = CoinInfoAndCoinDetail(
            CoinInfo(null, "메인코인"),
            listOf(CoinDetail(null, 1, 0.5f, 2.5f))
        ),
        dec = Const.DecFormat.Two.dec,
        {}, {}, { return@CoinListItem true }, {}
    )
}