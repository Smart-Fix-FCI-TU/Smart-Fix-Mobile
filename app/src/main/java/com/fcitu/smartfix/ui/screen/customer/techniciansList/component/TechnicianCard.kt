package com.fcitu.smartfix.ui.screen.customer.techniciansList.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.fcitu.smartfix.R
import com.fcitu.smartfix.ui.designSystem.components.button.PrimaryButton
import com.fcitu.smartfix.ui.designSystem.components.text.Text
import com.fcitu.smartfix.ui.designSystem.theme.Cairo

@Composable
fun TechnicianCard(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        TechnicianDetails()
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Available Now",
                style = TextStyle(
                    fontFamily = Cairo,
                    color = Color(0xFF16A34A),
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            PrimaryButton(
                text = "Order Now",
                onClick = { /* Handle booking */ },
                modifier = Modifier.padding(start = 16.dp),
                contentPadding = PaddingValues(12.dp),
                containerColor = Color(0xFFFF4400),
            )
        }
    }
}

@Composable
private fun TechnicianDetails() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AsyncImage(
            model = "https://example.com/technician.jpg",
            contentDescription = "Technician profile picture",
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(12.dp)),
        )
        TechnicianInfo()
        Spacer(modifier = Modifier.weight(1f))
        LocationDistance()
    }
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF2F4F7))
            .padding(vertical = 4.dp, horizontal = 12.dp),
    ) {
        Text(
            text = "5 Years Exp",
            style = TextStyle(
                fontFamily = Cairo,
                color = Color.Black,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}

@Composable
private fun LocationDistance() {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF2F4F7))
            .padding(vertical = 4.dp, horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_location),
            contentDescription = "Location",
            tint = Color(0xFFFF4502),
        )
        Text(
            text = "5 km",
            style = TextStyle(
                fontFamily = Cairo,
                color = Color(0xFFFF4502),
                fontSize = 12.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
private fun TechnicianInfo() {
    Column {
        Text(
            text = "John Doe",
            style = TextStyle(
                fontFamily = Cairo,
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 24.sp
            )
        )
        Text(
            text = "Electrician",
            style = TextStyle(
                fontFamily = Cairo,
                color = Color(0xFFEA580C),
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Medium
            )
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "Rating",
                tint = Color(0xFFFF4400),
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = "4.8",
                style = TextStyle(
                    fontFamily = Cairo,
                    color = Color(0xFF1C1B1F),
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = "(120 reviews)",
                style = TextStyle(
                    fontFamily = Cairo,
                    color = Color(0xFF6B7280),
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Normal
                )
            )
        }
    }
}