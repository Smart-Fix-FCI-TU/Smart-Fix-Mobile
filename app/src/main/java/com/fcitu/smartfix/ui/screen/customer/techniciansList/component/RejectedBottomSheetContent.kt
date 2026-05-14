package com.fcitu.smartfix.ui.screen.customer.techniciansList.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fcitu.smartfix.R
import com.fcitu.smartfix.ui.designSystem.components.button.PrimaryButton
import com.fcitu.smartfix.ui.designSystem.components.text.Text
import com.fcitu.smartfix.ui.designSystem.theme.Cairo

@Composable
fun RejectedBottomSheetContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.background(Color(0xFFF2F4F7)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RejectedMessage()
        TechnicianInfoSection(
            name = "Mahmoud Hassan",
            jobTitle = "Electrician",
            rating = 4.8f,
            distance = 1.2f,
            profilePhotoUrl = "https://images.pexels.com/videos/7055325/nature-natures-beauty-pine-plants-7055325.jpeg?auto=compress&cs=tinysrgb&w=600&loading=lazy",
            modifier = Modifier.padding(bottom = 12.dp)
        )
        PrimaryButton(
            text = "Choose another technician",
            onClick = {},
            containerColor = Color(0xFFFF4400),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        PrimaryButton(
            text = "Go Home",
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    border = BorderStroke(
                        width = 1.dp,
                        color = Color(0xFFFF4400)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ),
            containerColor = Color.White,
            contentColor = Color(0xFFFF4400)
        )
    }
}

@Composable
private fun RejectedMessage() {
    Box(
        modifier = Modifier
            .size(100.dp)
            .clip(CircleShape)
            .background(Color(0xFFDC2626)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_cancel),
            contentDescription = "Success",
            modifier = Modifier.size(64.dp),
            tint = Color.White
        )
    }
    Text(
        text = "Request Not Accepted",
        style = TextStyle(
            fontFamily = Cairo,
            fontSize = 36.sp,
            lineHeight = 40.sp,
            letterSpacing = (-0.9).sp,
            fontWeight = FontWeight.Bold
        ),
        textAlign = TextAlign.Center
    )
    Text(
        text = "Mahmoud Hassan is currently busy. You can choose another technician immediately.",
        style = TextStyle(
            fontFamily = Cairo,
            fontSize = 18.sp,
            lineHeight = 24.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFF9CA3AF)
        ),
        textAlign = TextAlign.Center
    )
}

@Preview
@Composable
private fun Preview() {
    RejectedBottomSheetContent()
}