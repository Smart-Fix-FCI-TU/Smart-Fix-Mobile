package com.fcitu.smartfix.ui.screen.customer.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fcitu.smartfix.R
import com.fcitu.smartfix.domain.entity.Order
import com.fcitu.smartfix.domain.model.OrderStatus
import com.fcitu.smartfix.ui.designSystem.components.button.Button
import com.fcitu.smartfix.ui.designSystem.components.text.Text

@Composable
fun ActiveOrderCard(order: Order, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0xFFFF4501), shape = RoundedCornerShape(16.dp))
            .clickable(enabled = true,
                onClickLabel = "Active Order Selection",
                role = Role.Button,
                interactionSource =  remember{ MutableInteractionSource() }
                ){
                onClick()
            },

        ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = { onClick() }) {

                    Image(
                        painter = painterResource(R.drawable.ic_arrow_left),
                        contentDescription = "Icon Arrow Left",
                        modifier = Modifier.padding(end = 5.dp)
                    )
                    Text(
                        text = "Details",
                        style = TextStyle(
                            color = Color.White.copy(alpha = 0.9f),
                            lineHeight = 20.sp,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    )
                }
                StatusChip(order.status)
            }
            Spacer(modifier = Modifier.padding(vertical = 20.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = order.details.title,
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 37.5.sp,


                        )
                )
                Text(
                    text = "Technician: ${order.technician.name}",
                    style = TextStyle(
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 20.sp
                    )
                )

            }
        }
    }

}

@Composable
private fun StatusChip(status: OrderStatus) {
    val label: String = when (status) {
        OrderStatus.ON_WAY -> "On the way"
        OrderStatus.ARRIVED -> "Arrived"
        OrderStatus.IN_PROGRESS -> "CurrentlyIn progress"
        else -> status.name
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(Color.White.copy(alpha = 0.2f))
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(
            text = "Current Status: $label",
            style = TextStyle(
                color = Color.White,
                fontWeight = FontWeight.Bold,
                lineHeight = 16.sp,
                fontSize = 12.sp
            )
        )
    }
}
