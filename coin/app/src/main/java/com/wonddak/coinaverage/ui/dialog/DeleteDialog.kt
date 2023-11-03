package com.wonddak.coinaverage.ui.dialog

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.wonddak.coinaverage.ui.common.CommonText
import com.wonddak.coinaverage.ui.theme.MATCH1

@Composable
fun DeleteDialog(
    onDismissRequest: () -> Unit,
    action: () -> Unit
) {
    DialogBase(
        onDismissRequest = onDismissRequest,
        title = "정말 삭제하시겠습니까?"
    ) {
        Row {
            DialogButton(onClick = { action() }) {
                CommonText(text = "삭제")
            }
            DialogButton(onClick = { onDismissRequest() }) {
                CommonText(text = "취소")
            }
        }
    }
}