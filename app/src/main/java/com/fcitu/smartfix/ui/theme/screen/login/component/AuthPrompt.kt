package com.fcitu.smartfix.ui.theme.screen.login.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fcitu.smartfix.ui.theme.Cairo
import com.fcitu.smartfix.ui.theme.designSystem.components.button.TextButton
import com.fcitu.smartfix.ui.theme.designSystem.components.text.Text

@Composable
fun AuthPrompt(
    message: String,
    actionLabel: String,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true
) {
    Row(
        modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = message,
            style = TextStyle(
                fontFamily = Cairo,
                fontWeight = FontWeight.Medium,
                fontSize = 10.sp,
                lineHeight = 16.sp
            ),
            color = Color(0xFF3E4252),
            modifier = Modifier.padding(end = 4.dp)
        )
        TextButton(
            text = actionLabel,
            onClick = onActionClick,
            isEnabled = isEnabled
        )
    }
}