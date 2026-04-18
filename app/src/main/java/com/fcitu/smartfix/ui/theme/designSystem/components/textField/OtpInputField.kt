package com.fcitu.smartfix.ui.theme.designSystem.components.textField

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fcitu.smartfix.ui.theme.Cairo

@Composable
fun OtpInputField(
    number: Int?,
    onNumberChanged: (Int?) -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(12.dp),
    containerColor: Color = Color(0xFFFFFFFF),
    onFocusChanged: (Boolean) -> Unit = {},
    onPressDeleteWhenEmpty: () -> Unit = {},
    focusRequester: FocusRequester = FocusRequester(),
) {
    val text: TextFieldValue by remember(number) {
        mutableStateOf(
            TextFieldValue(
                text = number?.toString().orEmpty(),
                selection = TextRange(
                    index = if (number != null) 1 else 0
                )
            )
        )
    }

    Box(
        modifier = modifier
            .size(48.dp, 66.dp)
            .background(containerColor, shape),
        contentAlignment = Alignment.Center
    ) {
        BasicTextField(
            value = text,
            onValueChange = { newText ->
                val newNumber = newText.text
                if (newNumber.length <= 1 && (newNumber.isEmpty() || newNumber.toIntOrNull() != null)) {
                    onNumberChanged(newNumber.toIntOrNull())
                }
            },
            cursorBrush = SolidColor(Color(0xFF000000)),
            textStyle = TextStyle(
                fontFamily = Cairo,
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
                lineHeight = 36.sp
            ).copy(
                textAlign = TextAlign.Center
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword
            ),
            modifier = Modifier
                .focusRequester(focusRequester)
                .onFocusChanged {
                    onFocusChanged(it.isFocused)
                }
                .onKeyEvent { event ->
                    val isDeleteKeyPressed = event.key == Key.Delete
                    if (isDeleteKeyPressed && number == null) {
                        onPressDeleteWhenEmpty()
                    }
                    false
                },
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .background(Color(0xFFFFFFFF), shape)
                        .size(48.dp, 66.dp),
                    contentAlignment = Alignment.Center
                ) {
                    innerTextField()
                }
            }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF2F4F7)
@Composable
private fun OtpInputFieldPreview() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        OtpInputField(
            number = null,
            onNumberChanged = { },
        )
        OtpInputField(
            number = 1,
            onNumberChanged = { },
        )
        OtpInputField(
            number = 2,
            onNumberChanged = { },
        )
    }
}