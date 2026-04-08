package com.wonddak.coinaverage.ui.common

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import com.wonddak.coinaverage.ui.theme.MATCH1
import com.wonddak.coinaverage.ui.theme.maple

@Composable
fun CommonText(
    text: String,
    textSize: Float? = null,
    color: Color = MATCH1,
    textAlign: TextAlign? = null,
    fontWeight: FontWeight? = null,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier,
        fontFamily = maple,
        textAlign = textAlign,
        color = color,
        fontSize = textSize?.let { TextUnit(it, TextUnitType.Sp) } ?: TextUnit.Unspecified,
        fontWeight = fontWeight
    )
}