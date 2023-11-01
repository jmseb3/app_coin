package com.wonddak.coinaverage.ui.fragment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun GraphView() {

    val itemList = listOf<String>(
        "손절매 못한 손실", "복구해야할 수익률",
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
    var input by remember {
        mutableStateOf("")
    }
    Column {

        TextField(value = input, onValueChange = { input = it })
        LazyVerticalGrid(
            columns = GridCells.Fixed(2)
        ) {
            items(itemList.size) { name ->
                Text(text = itemList[name])
            }
        }

    }
}