package com.fcitu.smartfix.ui.screen.customer.techniciansList.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fcitu.smartfix.R
import com.fcitu.smartfix.domain.entity.Technician
import com.fcitu.smartfix.ui.designSystem.components.text.Text
import com.fcitu.smartfix.ui.designSystem.theme.Cairo

@Composable
fun AcceptanceBottomSheetContent(
    technician: Technician,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.background(Color(0xFFF2F4F7)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AcceptanceMessage()
        TechnicianInfoSection(
            name = "${technician.user.firstName} ${technician.user.lastName}",
            jobTitle = technician.serviceCategory
                .name
                .lowercase()
                .replaceFirstChar { it.uppercase() },
            rating = technician.averageRating,
            distance = 1.2f,
            profilePhotoUrl = "https://images.pexels.com/videos/7055325/nature-natures-beauty-pine-plants-7055325.jpeg?auto=compress&cs=tinysrgb&w=600&loading=lazy",
        )
        OptionsBar()
    }
}

@Composable
private fun AcceptanceMessage() {
    Box(
        modifier = Modifier
            .size(100.dp)
            .clip(CircleShape)
            .background(Color(0xFF4CAF50)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_success),
            contentDescription = "Success",
            modifier = Modifier.size(64.dp),
            tint = Color.White
        )
    }
    Text(
        text = "Accepted! \uD83C\uDF89",
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
        text = "Mahmoud is on his way",
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

@Composable
private fun OptionsBar() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        OptionBarItem(
            containerSize = 64.dp,
            containerBackground = Color.White,
            icon = painterResource(R.drawable.ic_phone),
            iconTint = Color(0xFFFF4400),
            title = "Call",
            modifier = Modifier.padding(top = 16.dp),
        )
        OptionBarItem(
            containerSize = 80.dp,
            containerBackground = Color(0xFFFF4400),
            icon = painterResource(R.drawable.ic_map),
            iconTint = Color.White,
            title = "Track"
        )
        OptionBarItem(
            containerSize = 64.dp,
            containerBackground = Color.White,
            icon = painterResource(R.drawable.ic_chat),
            iconTint = Color(0xFFFF4400),
            title = "Chat",
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
private fun OptionBarItem(
    containerSize: Dp,
    containerBackground: Color,
    icon: Painter,
    iconTint: Color,
    title: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(containerSize)
                .clip(CircleShape)
                .background(containerBackground),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = icon,
                contentDescription = "Map",
                modifier = Modifier.size(24.dp),
                tint = iconTint
            )
        }
        Text(
            text = title,
            style = TextStyle(
                fontFamily = Cairo,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        )
    }
}