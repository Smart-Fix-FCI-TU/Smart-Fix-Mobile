package com.fcitu.smartfix.ui.screen.technician.profile.component

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.fcitu.smartfix.ui.designSystem.components.text.Text
import com.fcitu.smartfix.ui.designSystem.theme.Cairo

@Composable
fun StatItem(label: String, value: String, valueColor: Color = Color.Black) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = TextStyle(
                fontFamily = Cairo,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = valueColor
            )
        )
        Text(
            text = label,
            style = TextStyle(
                fontFamily = Cairo,
                fontWeight = FontWeight.SemiBold,
                fontSize = 10.sp,
                color = Color.Gray
            )
        )
    }
}
