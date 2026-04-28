package com.fcitu.smartfix.ui.theme.screen.login.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fcitu.smartfix.R
import com.fcitu.smartfix.ui.theme.Cairo
import com.fcitu.smartfix.ui.theme.designSystem.components.button.TextButton
import com.fcitu.smartfix.ui.theme.designSystem.components.text.Text
import com.fcitu.smartfix.ui.theme.designSystem.components.textField.TextField

@Composable
fun LabeledInputPassword(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    isPasswordVisible: Boolean,
    onClickForgetPassword: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = TextStyle(
                fontFamily = Cairo,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                lineHeight = 24.sp
            )
        )
        Spacer(modifier = Modifier.height(4.dp))
        TextField(
            value = value,
            onValueChanged = onValueChange,
            hint = "Enter your password",
            leadingIcon = painterResource(R.drawable.ic_lock),
            trailingIcon = painterResource(
                if (isPasswordVisible) R.drawable.ic_close_eye
                else R.drawable.ic_open_eye
            ),
            onTrailingIconClick = onTogglePasswordVisibility,
            visualTransformation = if (isPasswordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            showTrailingDivider = false,
            modifier = Modifier.fillMaxWidth(),
        )
        ForgetPasswordText(
            onClick = onClickForgetPassword,
        )
    }
}

@Composable
private fun ForgetPasswordText(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        TextButton(
            text = "Forget password?",
            isEnabled = true,
            onClick = onClick,
            contentColor = Color(0xFF000000),
        )
    }
}