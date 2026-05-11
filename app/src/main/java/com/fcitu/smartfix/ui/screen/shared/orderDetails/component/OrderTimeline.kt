package com.fcitu.smartfix.ui.screen.shared.orderDetails.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.VerticalDivider
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
import com.fcitu.smartfix.R
import com.fcitu.smartfix.domain.entity.Order
import com.fcitu.smartfix.ui.designSystem.components.text.Text
import com.fcitu.smartfix.ui.designSystem.theme.Cairo
import com.fcitu.smartfix.ui.utils.formatAsTime

@Composable
fun OrderTimeline(
    timeline: Order.OrderTimeline,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .padding(16.dp),
    ) {
        Text(
            text = "Service Timeline",
            style = TextStyle(
                fontFamily = Cairo,
                color = Color(0xFF2D2F2F),
                fontSize = 18.sp,
                lineHeight = 28.sp,
                fontWeight = FontWeight.Bold
            )
        )

        OrderStatusInfo(
            status = "Order Accepted",
            time = timeline.acceptedAt.formatAsTime()
        )

        Divider()

        OrderStatusInfo(
            status = "Technician On Way",
            time = timeline.onWayAt.formatAsTime()
        )

        Divider()

        OrderStatusInfo(
            status = "Technician Arrived",
            time = timeline.arrivedAt.formatAsTime()
        )

        Divider()

        OrderStatusInfo(
            status = "In Progress",
            time = timeline.startedAt.formatAsTime()
        )

        Divider()

        OrderStatusInfo(
            status = "Job Completed",
            time = timeline.completedAt.formatAsTime()
        )
    }
}

@Composable
private fun OrderStatusInfo(
    status: String,
    time: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_success),
            contentDescription = "Success",
            tint = Color(0xFF22C55E)
        )
        Text(
            text = status,
            style = TextStyle(
                fontFamily = Cairo,
                color = Color(0xFF2D2F2F),
                fontSize = 16.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.Medium
            ),
            maxLines = 1,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = time,
            style = TextStyle(
                fontFamily = Cairo,
                color = Color(0xFF5A5C5C),
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Normal
            )
        )
    }
}

@Composable
private fun Divider() {
    VerticalDivider(
        modifier = Modifier
            .size(width = 2.dp, height = 24.dp)
            .padding(horizontal = 8.dp),
        color = Color(0xFF22C55E),
        thickness = 2.dp
    )
}