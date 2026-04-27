package com.fcitu.smartfix.ui.theme.screen.login.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fcitu.smartfix.ui.theme.Cairo
import com.fcitu.smartfix.ui.theme.designSystem.components.text.Text

@Composable
fun WelcomeMessage(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "SMART FIX",
            style = TextStyle(
                fontFamily = Cairo,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            ),
            color = Color(0xFFFF4400),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 12.dp)
        )
        Text(
            text = "Welcome Back to SMART FIX",
            style = TextStyle(
                fontFamily = Cairo,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
            ),
            color = Color(0xFF0E1017)
        )
        Text(
            text = "Fill your information to login",
            style = TextStyle(
                fontFamily = Cairo,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
            ),
            color = Color(0xFF3E4252)
        )
    }
}