package com.wonddak.coinaverage.ui.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.wonddak.coinaverage.ui.common.CommonTextField
import com.wonddak.coinaverage.ui.theme.MATCH1
import com.wonddak.coinaverage.ui.theme.MATCH2
import com.wonddak.coinaverage.ui.theme.maple
import java.text.DecimalFormat

@Composable
fun GraphView() {
    val context = LocalContext.current
    val title1 = "손절매 못한 손실"
    val title2 = "복구해야할 수익률"
    val itemList = listOf(
        title1, title2,
        "3%", "3.09%",
        "5%", "5.26%",
        "10%", "11.11%",
        "20%", "25%",
        "30%", "42.86%",
        "40%", "66.67%",
        "50%", "100%",
        "60%", "150%",
        "70%", "233.33%",
        "80%", "400%",
        "90%", "900%"
    )
    val dec = DecimalFormat("###.00")

    var input by remember {
        mutableStateOf("")
    }
    var percent by remember {
        mutableStateOf("")
    }
    LaunchedEffect(input) {
        runCatching {
            if (input.isEmpty()) {
                percent = ""
            } else if (input.toFloat() >= 100) {
                Toast.makeText(context, "손실은 100%보다 클수 없습니다", Toast.LENGTH_SHORT).show()
                input = ""
            } else {
                val minusPer = input.toFloat() / 100
                val data = dec.format((minusPer / (1 - minusPer)) * 100)
                percent = data.toString()
            }
        }.onFailure {
            input = ""
            Toast.makeText(context, "숫자만 입력해주세요", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MATCH2),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CommonTextField(
            value = input,
            onValueChange = {
                input = if (it.isEmpty()) {
                    ""
                } else {
                    when (it.toFloatOrNull()) {
                        null -> {
                            input
                        }

                        else -> it
                    }
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            labelText = title1,
            trailingIcon = {
                Text(
                    text = "%",
                    color = MATCH1
                )
            },
        )
        Spacer(modifier = Modifier.height(5.dp))
        CommonTextField(
            value = percent,
            onValueChange = { },
            readOnly = true,
            labelText = title2,
            trailingIcon = {
                Text(
                    text = "%",
                    color = MATCH1
                )
            },
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(10.dp)
                .background(MATCH2)
                .border(2.dp, MATCH1, RoundedCornerShape(2.dp))
                .clip(RoundedCornerShape(2.dp))
        ) {
            items(itemList.size) { name ->
                Column {
                    Text(
                        text = itemList[name],
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MATCH1)
                            .padding(
                                PaddingValues(
                                    horizontal = 0.dp,
                                    vertical = 3.dp
                                )
                            ),
                        textAlign = TextAlign.Center,
                        fontFamily = maple
                    )
                    HorizontalDivider(
                        color = MATCH2
                    )
                }
            }
        }

    }
}