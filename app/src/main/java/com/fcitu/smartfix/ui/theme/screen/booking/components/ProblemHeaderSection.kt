package com.fcitu.smartfix.ui.theme.screen.booking.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fcitu.smartfix.ui.theme.Cairo
import com.fcitu.smartfix.ui.theme.designSystem.components.text.Text

@Composable
fun ProblemHeaderSection(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Describe Problem",
            style = TextStyle(
                fontFamily = Cairo,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Enter problem details to enable us to estimate cost and select the right technician",
            style = TextStyle(
                fontFamily = Cairo,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                color = Color.Gray
            )
        )
    }
}