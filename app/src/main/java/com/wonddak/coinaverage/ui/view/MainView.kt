package com.wonddak.coinaverage.ui.view

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.wonddak.coinaverage.R
import com.wonddak.coinaverage.getAllCount
import com.wonddak.coinaverage.getAvg
import com.wonddak.coinaverage.getCoinId
import com.wonddak.coinaverage.getCountString
import com.wonddak.coinaverage.getPriceString
import com.wonddak.coinaverage.getTotalPrice
import com.wonddak.coinaverage.getTotalPriceSum
import com.wonddak.coinaverage.noRippleClickable
import com.wonddak.coinaverage.room.CoinDetail
import com.wonddak.coinaverage.room.CoinInfoAndCoinDetail
import com.wonddak.coinaverage.toFormat
import com.wonddak.coinaverage.ui.common.CommonText
import com.wonddak.coinaverage.ui.common.CommonTextField
import com.wonddak.coinaverage.ui.dialog.DialogButton
import com.wonddak.coinaverage.ui.main.BackOnPressedExitApp
import com.wonddak.coinaverage.ui.theme.MATCH1
import com.wonddak.coinaverage.ui.theme.MATCH2
import com.wonddak.coinaverage.viewmodel.CoinViewModel
import kotlinx.coroutines.launch


@Composable
fun MainView(
    viewModel: CoinViewModel,
    updateTitle :(name:String) -> Unit
) {
    val nowInfo by viewModel.nowInfo.collectAsState()
    val dec by viewModel.dec.collectAsState()
    val next by viewModel.next.collectAsState()

    LaunchedEffect(nowInfo) {
        nowInfo?.coinInfo?.coinName?.let { updateTitle(it) }
    }
    nowInfo?.let { item ->
        MainContent(
            item,
            dec,
            next,
            actionAdd = {
                viewModel.addDetail(item.getCoinId())
            },
            actionResetAll = {
                viewModel.resetCoin(item.getCoinId())
            },
            actionItemReset = {
                viewModel.resetCoinDetail(it)
            },
            actionItemDelete = {
                viewModel.deleteCoinDetail(it)
            },
            actionItemUpdateCount = { id, count ->
                viewModel.updateCoinDetailCount(id, count)
            },
            actionItemUpdatePrice = { id, price ->
                viewModel.updateCoinDetailPrice(id, price)
            }
        )
    }
}

@Composable
private fun MainContent(
    item: CoinInfoAndCoinDetail,
    dec: String,
    next: Boolean,
    actionAdd: () -> Unit,
    actionResetAll: () -> Unit,
    actionItemReset: (detailId: Int) -> Unit,
    actionItemDelete: (detailId: Int) -> Unit,
    actionItemUpdatePrice: (detailId: Int, price: Float) -> Unit,
    actionItemUpdateCount: (detailId: Int, count: Float) -> Unit,
) {
    val context = LocalContext.current
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MATCH2)
            .padding(10.dp)
            .imePadding(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Column(
                modifier = Modifier
                    .border(
                        width = 2.dp,
                        color = MATCH1,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MainInfoRow(
                    section = "평균 매수 단가 :",
                    info = item.getAvg().toFormat(dec, " 원")
                )
                MainInfoRow(
                    section = "총 매수 금액 :",
                    info = item.getTotalPriceSum().toFormat(dec, " 원")
                )
                MainInfoRow(
                    section = "총 매수 량 :",
                    info = item.getAllCount().toFormat(dec, " 개")
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                state = listState
            ) {
                val list = item.coinDetailList
                items(list.size) {
                    val detailItem = list[it]
                    val leftImeAction = if (next) ImeAction.Next else ImeAction.Done
                    val rightImeAction =
                        if (!next || it == list.size - 1) ImeAction.Done else ImeAction.Next
                    MainItemRow(
                        item = detailItem,
                        position = it + 1,
                        dec = dec,
                        leftImeAction = leftImeAction,
                        rightImeAction = rightImeAction,
                        actionReset = {
                            actionItemReset(detailItem.id!!)
                        },
                        actionDelete = {
                            if (list.size > 1) {
                                actionItemDelete(detailItem.id!!)
                            } else {
                                Toast.makeText(context, "최소 하나는 존재해야합니다.", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        },
                        updatePrice = { price ->
                            price.toFloatOrNull()?.let {
                                actionItemUpdatePrice(detailItem.id!!, it)
                            }
                        },
                        updateCount = { count ->
                            count.toFloatOrNull()?.let {
                                actionItemUpdateCount(detailItem.id!!, it)
                            }
                        },
                        onFocus = {
                            scope.launch {
                                listState.scrollToItem(it)
                            }
                        }
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MATCH2),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            DialogButton(
                text = "추가",
                modifier = Modifier.weight(1f)
            ) {
                actionAdd()
            }
            DialogButton(
                text = "전체 초기화",
                modifier = Modifier.weight(1f)
            ) {
                actionResetAll()
            }
        }
    }
}

@Composable
private fun MainInfoRow(
    modifier: Modifier = Modifier,
    section: String,
    info: String,
    color: Color = MATCH1
) {
    Row(modifier = modifier) {
        CommonText(
            text = section,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Left,
            color = color
        )
        CommonText(
            text = info,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End,
            color = color
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MainItemRow(
    item: CoinDetail,
    position: Int,
    dec: String,
    leftImeAction: ImeAction,
    rightImeAction: ImeAction,
    onFocus: () -> Unit,
    actionReset: () -> Unit,
    actionDelete: () -> Unit,
    updatePrice: (price: String) -> Unit,
    updateCount: (count: String) -> Unit
) {
    val focusManager = LocalFocusManager.current

    var price by remember {
        mutableStateOf(TextFieldValue(item.getPriceString()))
    }
    var count by remember {
        mutableStateOf(TextFieldValue(item.getCountString()))
    }

    LaunchedEffect(item) {
        price = TextFieldValue(item.getPriceString())
        count = TextFieldValue(item.getCountString())
    }
    fun updateValue(value: TextFieldValue): TextFieldValue? {
        return if (value.text.isEmpty()) {
            TextFieldValue("")
        } else {
            val regex = Regex("\\d+?\\.?\\d*")
            if (regex.matches(value.text)) {
                val split = value.text.split(".", limit = 2)
                val number = split[0].trimStart('0').ifEmpty { "0" }
                val text = if (split.size == 2) {
                    "$number.${split[1]}"
                } else {
                    number
                }
                value.copy(
                    text = text,
                    selection = TextRange(text.length)
                )
            } else {
                null
            }
        }
    }
    Card(
        modifier = Modifier.combinedClickable(
            onClick = {

            },
            onLongClick = {
                actionDelete()
            }
        ),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MATCH1
        ),
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CommonText(
                    text = "# $position",
                    color = MATCH2,
                )
                CommonTextField(
                    modifier = Modifier
                        .weight(1f)
                        .onFocusChanged { focusState ->
                            if (focusState.isFocused) {
                                onFocus()
                                val text = price.text
                                price = price.copy(selection = TextRange(0, text.length))
                            } else {
                                updatePrice(price.text)
                            }
                        },
                    color = MATCH2,
                    value = price,
                    onValueChange = {
                        val update = updateValue(it)
                        price = when (update) {
                            null -> price
                            else -> update
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = leftImeAction
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Next)
                        },
                        onDone = {
                            focusManager.clearFocus()
                        }
                    ),
                    labelText = "매수가",
                )
                CommonTextField(
                    modifier = Modifier
                        .weight(1f)
                        .onFocusChanged { focusState ->
                            if (focusState.isFocused) {
                                onFocus()
                                val text = count.text
                                count = count.copy(selection = TextRange(0, text.length))
                            } else {
                                updateCount(count.text)
                            }
                        },
                    color = MATCH2,
                    value = count,
                    onValueChange = {
                        val update = updateValue(it)
                        count = when (update) {
                            null -> count
                            else -> update
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = rightImeAction
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Next)
                        },
                        onDone = {
                            focusManager.clearFocus()
                        }
                    ),
                    labelText = "매수량",
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    modifier = Modifier.size(23.dp),
                    onClick = {
                        actionReset()
                        price = TextFieldValue("")
                        count = TextFieldValue("")
                        focusManager.clearFocus()
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_restore_24),
                        contentDescription = null,
                        Modifier.size(18.dp),
                        tint = MATCH2
                    )
                }
                MainInfoRow(
                    modifier = Modifier.padding(10.dp),
                    section = "총 가격",
                    info = item.getTotalPrice().toFormat(dec, "원"),
                    color = MATCH2
                )
            }
        }
    }
}
