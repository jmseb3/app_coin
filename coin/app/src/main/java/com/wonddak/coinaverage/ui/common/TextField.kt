package com.wonddak.coinaverage.ui.common

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import com.wonddak.coinaverage.ui.theme.MATCH1


@Composable
fun CommonTextField(
    value: String,
    onValueChange: (value: String) -> Unit,
    color :Color = MATCH1,
    modifier: Modifier = Modifier,
    labelText: String? = null,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    val colors = OutlinedTextFieldDefaults.colors(
        unfocusedBorderColor = color,
        focusedBorderColor = color,
        disabledBorderColor = color,
        disabledTextColor = color,
        focusedTextColor = color,
        unfocusedTextColor = color,
        cursorColor = color,
    )
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        enabled = enabled,
        label = {
            if (labelText != null) {
                CommonText(
                    text = labelText,
                    color = color
                )
            }
        },
        colors = colors,
        trailingIcon = trailingIcon
    )
}

@Composable
fun CommonTextField(
    value: TextFieldValue,
    onValueChange: (value: TextFieldValue) -> Unit,
    color :Color = MATCH1,
    modifier: Modifier = Modifier,
    labelText: String? = null,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    val colors = OutlinedTextFieldDefaults.colors(
        unfocusedBorderColor = color,
        focusedBorderColor = color,
        disabledBorderColor = color,
        disabledTextColor = color,
        focusedTextColor = color,
        unfocusedTextColor = color,
        cursorColor = color,
    )
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        enabled = enabled,
        label = {
            if (labelText != null) {
                CommonText(
                    text = labelText,
                    color = color
                )
            }
        },
        colors = colors,
        trailingIcon = trailingIcon
    )
}