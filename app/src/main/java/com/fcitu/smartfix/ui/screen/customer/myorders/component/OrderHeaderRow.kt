package com.fcitu.smartfix.ui.screen.customer.myorders.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fcitu.smartfix.domain.model.ServiceCategory
import com.fcitu.smartfix.ui.designSystem.components.text.Text


@Composable
 fun OrderHeaderRow(
    serviceCategory: ServiceCategory,
    title: String,
    time: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ServiceCategoryIndicator(serviceCategory)

        Text(
            modifier = Modifier
                .weight(1f)
                .padding(top = 10.dp)
                .padding(horizontal = 8.dp),
            text = title,
            style = TextStyle(
                color = Color(0xFF1C1B1F),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 20.sp
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        TimeBadge(time = time)
    }
}

@Composable
private fun TimeBadge(time: String) {
    Box(
        modifier = Modifier
            .background(
                color = Color(0xFFF2F4F7),
                shape = RoundedCornerShape(percent = 50)
            )
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = time,
            style = TextStyle(
                color = Color(0xFFFF4A08),
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 11.2.sp
            )
        )
    }
}
