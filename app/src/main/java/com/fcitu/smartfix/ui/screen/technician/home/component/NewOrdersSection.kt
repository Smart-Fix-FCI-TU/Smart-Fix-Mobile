package com.fcitu.smartfix.ui.screen.technician.home.component

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fcitu.smartfix.R
import com.fcitu.smartfix.domain.entity.Order
import com.fcitu.smartfix.ui.designSystem.components.button.Button
import com.fcitu.smartfix.ui.designSystem.components.text.Text
import com.fcitu.smartfix.ui.screen.customer.home.component.HomeSection
import com.fcitu.smartfix.ui.screen.customer.myorders.component.ServiceCategoryIndicator
import com.fcitu.smartfix.ui.screen.technician.home.TechHomeInteractionListener
import com.fcitu.smartfix.ui.screen.technician.home.TechHomeUiState
import kotlinx.coroutines.delay

@Composable
fun NewOrdersSection(
    state: TechHomeUiState,
    listener: TechHomeInteractionListener,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        HomeSection(title = "New Orders")

        when {
            state.isLoadingOrders -> {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        color = Color(0xFFFF4501),
                        modifier = Modifier.padding(32.dp),
                    )
                }
            }

            state.isTransitioning -> {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    state.pendingOrders.forEach { order ->
                        key(order.id) {
                            val isRejected = order.id in state.rejectedOrderIds
                            val isAccepted = order.id == state.acceptedOrderId

                            NewOrderCard(
                                order = order,
                                isRejected = isRejected,
                                isAccepted = isAccepted,
                                onAccept = { listener.onAcceptOrder(order.id) },
                                onReject = { listener.onRejectOrder(order.id) },
                                onViewDetails = { listener.onViewOrderDetails(order) },
                                onTimeout = { orderId ->
                                    if (!isRejected && !isAccepted) {
                                        listener.onOrderTimeout(orderId)
                                    }
                                },
                            )
                        }
                    }
                }
            }

            !state.isAvailable || !state.hasOrders -> {
                NoOrdersCard()
            }

            else -> {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    state.pendingOrders.forEach { order ->
                        key(order.id) {
                            val isRejected = order.id in state.rejectedOrderIds
                            val isAccepted = order.id == state.acceptedOrderId

                            NewOrderCard(
                                order = order,
                                isRejected = isRejected,
                                isAccepted = isAccepted,
                                onAccept = { listener.onAcceptOrder(order.id) },
                                onReject = { listener.onRejectOrder(order.id) },
                                onViewDetails = { listener.onViewOrderDetails(order) },
                                onTimeout = { orderId ->
                                    if (!isRejected && !isAccepted) {
                                        listener.onOrderTimeout(orderId)
                                    }
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun NewOrderCard(
    modifier: Modifier = Modifier,
    order: Order,
    isRejected: Boolean,
    isAccepted: Boolean,
    onAccept: () -> Unit = {},
    onReject: () -> Unit = {},
    onViewDetails: () -> Unit = {},
    onTimeout: (String) -> Unit = {},
) {

    val acceptButtonColor by animateColorAsState(
        targetValue = when {
            isAccepted -> Color(0xFF4CAF50)
            isRejected -> Color.Red
            else -> Color(0xFFFF4A08)
        },
        animationSpec = tween(300),
        label = "acceptBtnColor"
    )

    Box(
        modifier = modifier
            .padding(10.dp)
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .background(color = Color.White, shape = RoundedCornerShape(16.dp))
            .clickable(
                enabled = !isRejected,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { onViewDetails() }
            )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // ── Header ────────────────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    ServiceCategoryIndicator(order.details.serviceCategory)

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = order.details.title,
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Color(0xFF1C1B1F),
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = Color(0xFF9E9E9E),
                                modifier = Modifier.size(14.dp),
                            )
                            Text(
                                text = order.details.address.fullAddress,
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    color = Color(0xFF9E9E9E),
                                ),
                            )
                        }
                    }
                }

                if (!isAccepted && !isRejected) {
                    TimerChip(order = order, onTimeout = onTimeout)
                }
            }

            // ── Action Buttons ────────────────────────────────────────────────

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (!isAccepted) {
                    Button(
                        onClick = onReject,
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(vertical = 15.dp),
                        shape = RoundedCornerShape(12.dp),
                        containerColor = Color.Red
                    ) {
                        Text(
                            text = if (isRejected) " Rejected ✕" else {
                                "Reject"
                            },
                            style = TextStyle(
                                color = Color.White,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                            ),
                        )
                    }
                }
                if (!isRejected) {
                    Button(
                        onClick = { if (!isRejected) onAccept() },
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(vertical = 15.dp),
                        shape = RoundedCornerShape(12.dp),
                        containerColor = acceptButtonColor,
                    ) {
                        Text(
                            text = if (isAccepted) "Accepted ✓" else {
                                "Accept Order"
                            },
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color.White,
                            ),
                        )
                    }

                }
            }
            if (!isRejected && !isAccepted) {
                Button(
                    onClick = onViewDetails,
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 15.dp),
                    shape = RoundedCornerShape(12.dp),
                    containerColor = Color(0xFFF2F4F7)
                ) {
                    Text(
                        text = "Details",
                        style = TextStyle(
                            color = Color(0xFF475569),
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                        ),
                    )
                }
            }

        }

        if (isRejected) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color(0xFFBEC0CC).copy(alpha = 0.4f))
            )
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun TimerChip(
    order: Order,
    onTimeout: (String) -> Unit,
    timeoutMinutes: Int = 1,
) {
    var remainingSeconds by remember(order.id) {
        mutableIntStateOf(timeoutMinutes * 60)
    }

    LaunchedEffect(order.id) {
        while (remainingSeconds > 0) {
            delay(1_000)
            remainingSeconds--
        }
        onTimeout(order.id)
    }

    val minutes = remainingSeconds / 60
    val seconds = remainingSeconds % 60

    Box(
        modifier = Modifier
            .background(
                color = Color(0xFFF2F4F7),
                shape = RoundedCornerShape(10.dp),
            )
            .padding(horizontal = 10.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "$minutes:${String.format("%02d", seconds)}\nleft",
            style = TextStyle(
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF4501),
                textAlign = TextAlign.Center,
                lineHeight = 14.sp,
            ),
        )
    }
}

@Composable
private fun NoOrdersCard() {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .shadow(2.dp, shape = RoundedCornerShape(16.dp))
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
                text = "No orders available",
                style = TextStyle(
                    color = Color(0xFF1C1B1F),
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    lineHeight = 36.sp
                )
            )
            Spacer(modifier = Modifier.padding(vertical = 10.dp))
            Text(
                text = "There are no available orders right now.\nActivate your status to start receiving jobs.",
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