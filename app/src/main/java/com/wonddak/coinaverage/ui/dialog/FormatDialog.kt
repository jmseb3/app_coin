package com.wonddak.coinaverage.ui.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import com.wonddak.coinaverage.Const

@Composable
fun FormatDialog(
    nowFormat :String,
    onDismissRequest: () -> Unit,
    action: (format: String) -> Unit
) {
    val title = Const.DecFormat.entries.toTypedArray()
    DialogBase(
        onDismissRequest = onDismissRequest,
        title = "표시 형식 선택"
    ) {
        title.forEachIndexed { index, format ->
            DialogButton(
                text = "소수점 $index 개",
                fontWeight = if(format.dec == nowFormat) FontWeight.Bold else FontWeight.Normal
            ) {
                action(format.dec)
            }
        }
    }
}