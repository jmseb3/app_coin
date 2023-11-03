package com.wonddak.coinaverage.ui.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ){
            DialogButton(
                text = "삭제",
                modifier = Modifier.weight(1f)
            ) {
                action()
                onDismissRequest()
            }
            DialogButton(
                text = "취소",
                modifier = Modifier.weight(1f)
            ) {
                onDismissRequest()
            }
        }
    }
}