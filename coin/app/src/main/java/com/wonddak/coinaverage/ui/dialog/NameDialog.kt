package com.wonddak.coinaverage.ui.dialog

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.wonddak.coinaverage.room.CoinInfoAndCoinDetail
import com.wonddak.coinaverage.ui.common.CommonText
import com.wonddak.coinaverage.ui.theme.MATCH1

@Composable
fun NameDialog(
    coinInfoAndCoinDetail: CoinInfoAndCoinDetail?,
    onDismissRequest: () -> Unit,
    action: (name: String) -> Unit
) {
    val color = OutlinedTextFieldDefaults.colors(
        unfocusedBorderColor = MATCH1,
        focusedBorderColor = MATCH1,
        disabledBorderColor = MATCH1,
        disabledTextColor = MATCH1,
        focusedTextColor = MATCH1,
        unfocusedTextColor = MATCH1,
        cursorColor = MATCH1,
    )
    val title = if (coinInfoAndCoinDetail == null) "새 코인 추가" else "수정할 이름을 설정해주세요."
    val okText = if (coinInfoAndCoinDetail == null) "추가" else "수정"
    var name by remember {
        mutableStateOf(coinInfoAndCoinDetail?.coinInfo?.coinName ?: "")
    }
    DialogBase(
        onDismissRequest = onDismissRequest,
        title = title
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            label = {
                Text(
                    text = "이름",
                    color = MATCH1
                )
            },
            colors = color
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row {
            DialogButton(
                onClick = { action(name) },
                modifier = Modifier.weight(1f),
                enabled = name.isNotEmpty()
            ) {
                CommonText(text = okText)
            }
            DialogButton(
                onClick = { onDismissRequest() },
                modifier = Modifier.weight(1f)
            ) {
                CommonText(text = "취소")
            }
        }
    }
}