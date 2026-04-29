package com.fcitu.smartfix.ui.designSystem.components.textField

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun TextField(
    value: String,
    hint: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: Painter? = null,
    trailingIcon: Painter? = null,
    title: String? = null,
    leadingIconTint: Color = Color(0xFF0E1017),
    trailingIconTint: Color = Color(0xFF818599),
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    showTrailingDivider: Boolean = true,
    errorMessage: String? = null,
    shape: Shape = RoundedCornerShape(12.dp),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    focusRequester: FocusRequester = FocusRequester(),
    onFocusChanged: (Boolean) -> Unit = {},
    onTrailingIconClick: (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    maxCharacters: Int = Int.MAX_VALUE,
) {
    BasicTextField(
        value = value,
        hint = hint,
        onValueChanged = onValueChanged,
        modifier = modifier,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        title = title,
        leadingIconTint = leadingIconTint,
        trailingIconTint = trailingIconTint,
        enabled = enabled,
        readOnly = readOnly,
        isError = isError,
        showTrailingDivider = showTrailingDivider,
        errorMessage = errorMessage,
        shape = shape,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        focusRequester = focusRequester,
        onFocusChanged = onFocusChanged,
        onTrailingIconClick = onTrailingIconClick,
        visualTransformation = visualTransformation,
        maxCharacters = maxCharacters
    )
}