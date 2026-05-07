package com.fcitu.smartfix.ui.screen.customer.home.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fcitu.smartfix.ui.designSystem.components.button.Button
import com.fcitu.smartfix.ui.designSystem.components.text.Text

@Composable
fun ChooseServiceButton(isServiceSelected: Boolean, onClick: () -> Unit) {
    val buttonColor = if (isServiceSelected) Color(0xFFFF4806) else Color(0xFFBEC0CC)
    val contentColor = if (isServiceSelected) Color(0xFFFFFFFF) else Color(0xFF818599)
    Button(
        onClick = { onClick() },
        shape = RoundedCornerShape(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 24.dp),
        isEnabled = isServiceSelected, containerColor = buttonColor,
        contentColor = contentColor
    ) {
        Text(
            "Choose a Service",
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp, lineHeight = 24.sp)
        )
    }
}