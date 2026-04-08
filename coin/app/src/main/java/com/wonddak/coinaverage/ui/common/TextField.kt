package com.wonddak.coinaverage.ui.common

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults.outlinedTextFieldColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import com.wonddak.coinaverage.ui.theme.MATCH1


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (value: String) -> Unit,
    color: Color = MATCH1,
    labelText: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    val colors = outlinedTextFieldColors(
        unfocusedBorderColor = color,
        focusedBorderColor = color,
        disabledBorderColor = color,
        disabledTextColor = color,
        cursorColor = color,
        focusedTextColor = color,
        unfocusedTextColor = color
    )
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        enabled = enabled,
        readOnly = readOnly,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTextField(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    onValueChange: (value: TextFieldValue) -> Unit,
    color: Color = MATCH1,
    labelText: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    val colors = outlinedTextFieldColors(
        unfocusedBorderColor = color,
        focusedBorderColor = color,
        disabledBorderColor = color,
        disabledTextColor = color,
        cursorColor = color,
    )
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        label = {
            if (labelText != null) {
                CommonText(
                    text = labelText,
                    color = color
                )
            }
        },
        colors = colors,
    )
}