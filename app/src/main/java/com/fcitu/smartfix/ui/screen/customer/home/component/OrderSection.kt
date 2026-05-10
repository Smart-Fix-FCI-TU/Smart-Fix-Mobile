package com.fcitu.smartfix.ui.screen.customer.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
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
import com.fcitu.smartfix.ui.designSystem.components.text.Text
import com.fcitu.smartfix.ui.screen.customer.home.HomeInteractionListener
import com.fcitu.smartfix.ui.screen.customer.home.HomeUiState

@Composable
fun OrderSection(
    state: HomeUiState,
    listener: HomeInteractionListener,
    modifier: Modifier = Modifier
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        HomeSection(
            modifier = modifier,
            title = "Active Orders",
            actionName = if (state.hasActiveOrders && state.activeOrders.size > 1) "View All" else null,
            onActionNameClick = { listener.onViewAllOrdersClicked(state.activeOrders) })
        when {
            state.isLoadingActiveOrders -> {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(70.dp)
                            .padding(top = 30.dp),
                        color = Color.White,
                        strokeWidth = 5.dp,
                        trackColor = Color(0xFFFF4501)
                    )
                }
            }

            !state.hasActiveOrders -> {
                EmptyOrderCard()
            }

            else -> {
                if (state.activeOrders.size == 1) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 30.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center, // ← في النص
                    ) {

                        val order by remember(state.activeOrders[0].status) {
                            derivedStateOf { state.activeOrders[0] }
                        }
                        ActiveOrderCard(order = order) {
                            listener.onOrderClicked(order.id)
                        }
                    }
                } else {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        contentPadding = PaddingValues(4.dp)
                    ) {
                        items(items = state.activeOrders, key = { it.id }) { order ->
                            ActiveOrderCard(order) {
                                listener.onOrderClicked(order.id)
                            }

                        }
                    }
                }
            }
        }
    }
}


// Display an Empty order card when there are no active orders at the moment.
@Composable
fun EmptyOrderCard() {
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .background(color = Color.White, shape = RoundedCornerShape(16.dp))
            .padding(32.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.empty_card_image),
                contentDescription = "Empty Card Image"
            )
            Spacer(modifier = Modifier.padding(vertical = 10.dp))
            Text(
                text = "No Orders Yet!",
                style = TextStyle(
                    color = Color(0xFF1C1B1F),
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    lineHeight = 36.sp
                )
            )
            Spacer(modifier = Modifier.padding(vertical = 10.dp))
            Text(
                text = "Order a technician now, and " +
                        "\n    they will arrive quickly.",
                style = TextStyle(
                    color = Color(0xFF49454E),
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    lineHeight = 26.sp
                )
            )

        }
    }
}


//Display active order cards when there are currently active orders.
@Composable
fun ActiveOrderCard(order: Order, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(5.dp)
            .width(320.dp)
            .height(150.dp)
            .background(color = Color(0xFFFF4501), shape = RoundedCornerShape(16.dp))
            .clickable(
                enabled = true,
                onClickLabel = "Active Order Selection",
                role = Role.Button,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
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

            // Details Text and Status Chip----------------------------------------------------
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
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
            Spacer(modifier = Modifier.height(20.dp))


            // Order Title and Technician Name---------------------------------------------------
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = order.details.title,
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 37.5.sp,
                    ),
                    maxLines = 1
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
            .padding(start = 30.dp, end = 12.dp, top = 4.dp, bottom = 4.dp)
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