package com.fcitu.smartfix.ui.screen.customer.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fcitu.smartfix.R
import com.fcitu.smartfix.ui.designSystem.components.text.Text


@Composable
fun PendingOrderSection(
    modifier: Modifier = Modifier,
    orderId: String,
    onPendingOrderClick: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        HomeSection(title = "Pending Order", modifier = modifier)
        PendingOrderCard(modifier = modifier, orderId = orderId) {
            onPendingOrderClick(orderId)
        }


    }
}

@Composable
fun PendingOrderCard(modifier: Modifier = Modifier, orderId: String, onClick: (String) -> Unit) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color(0xFFFF4501), shape = RoundedCornerShape(16.dp))
            .clickable(
                enabled = true,
                onClickLabel = "Active Order Selection",
                role = Role.Button,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClick(orderId)
            },

        ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 6.dp)
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.pending_request_icon),
                contentDescription = "Pending Request Icon"
            )
            Column(
                modifier = Modifier.padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "You have a pending request",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 37.5.sp,


                        )
                )
                Text(
                    text = "Tap to choose a technician ",
                    style = TextStyle(
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 20.sp
                    )
                )

            }
            Image(
                painter = painterResource(R.drawable.right_arrow_icon),
                contentDescription = "Right Arrow Icon", modifier = Modifier.size(20.dp)
            )

        }
    }
}