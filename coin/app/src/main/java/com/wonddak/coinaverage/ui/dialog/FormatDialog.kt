package com.wonddak.coinaverage.ui.dialog

import androidx.compose.runtime.Composable
import com.wonddak.coinaverage.Const

@Composable
fun FormatDialog(
    onDismissRequest: () -> Unit,
    action: (format: String) -> Unit
) {
    val title = Const.DecFormat.values()
    DialogBase(
        onDismissRequest = onDismissRequest,
        title = "표시 형식 선택"
    ) {
        title.forEachIndexed { index, format ->
            DialogButton(
                text = "소수점 $index 개"
            )
            {
                action(format.dec)
            }
        }
    }
}