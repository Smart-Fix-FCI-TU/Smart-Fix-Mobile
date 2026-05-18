package com.fcitu.smartfix.ui.screen.customer.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fcitu.smartfix.R
import com.fcitu.smartfix.ui.designSystem.components.button.Button
import com.fcitu.smartfix.ui.designSystem.components.text.Text

@Composable
fun NetworkOutageScreen(onTryAgain:()-> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.image_of_no_network),
            contentDescription = "Image of no network"
        )
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            "Please check your connection and try again",
            style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Normal, lineHeight = 24.sp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = { onTryAgain() },
            shape = RoundedCornerShape(16.dp),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
            contentColor = Color.White,
            containerColor = Color(0xFFFF4806)
        ) {
            Text(
                text = "Try Again",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }

    }
}
