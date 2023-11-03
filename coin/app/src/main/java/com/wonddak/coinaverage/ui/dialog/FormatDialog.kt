package com.wonddak.coinaverage.ui.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.wonddak.coinaverage.Const
import com.wonddak.coinaverage.ui.theme.MATCH1
import com.wonddak.coinaverage.ui.theme.MATCH2

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