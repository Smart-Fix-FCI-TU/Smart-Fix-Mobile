package com.fcitu.smartfix.ui.screen.shared.orderDetails.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fcitu.smartfix.ui.designSystem.components.text.Text
import com.fcitu.smartfix.ui.designSystem.theme.Cairo

@Composable
fun OrderInfo(
    orderId: String,
    title: String,
    description: String,
    createdAt: String,
    address: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Info(
                label = "TITLE",
                value = title
            )
            Info(
                label = "ORDER ID",
                value = orderId.take(6),
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Info(
            label = "DESCRIPTION",
            value = description
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Info(
                label = "DATE",
                value = createdAt
            )
            Info(
                label = "ADDRESS",
                value = address,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

@Composable
private fun Info(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = TextStyle(
                fontFamily = Cairo,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF5A5C5C)
            ),
            maxLines = 1
        )
        Text(
            text = value,
            style = TextStyle(
                fontFamily = Cairo,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2D2F2F)
            ),
            maxLines = 1
        )
    }
}