package com.fcitu.smartfix.ui.screen.customer.myorders.component

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fcitu.smartfix.R
import com.fcitu.smartfix.domain.entity.Order
import com.fcitu.smartfix.domain.entity.Technician
import com.fcitu.smartfix.domain.model.ServiceCategory
import com.fcitu.smartfix.ui.designSystem.components.text.Text
import com.fcitu.smartfix.ui.screen.customer.home.component.ProfilePhoto
import com.fcitu.smartfix.ui.utils.makePhoneCall
import com.fcitu.smartfix.ui.utils.toOrderTimeFormat

@Composable
fun ActiveOrderCard(
    order: Order,
    technician: Technician? = null,
    onActiveOrderClicked: (String) -> Unit,
    onChatClicked: (String, String) -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .fillMaxWidth()
            .background(color = Color.White, shape = RoundedCornerShape(32.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { onActiveOrderClicked(order.id) }
            )
            .padding(20.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //  Header Row: Category + Title + Time
            OrderHeaderRow(
                serviceCategory = order.details.serviceCategory,
                title = order.details.title,
                time = order.timeline.acceptedAt.toOrderTimeFormat()
            )

            // Divider
            Image(
                painter = painterResource(id = R.drawable.line),
                contentDescription = "Divider"
            )

            //  Technician Info Row
            TechnicianInfoRow(
                orderId = order.id,
                technician = technician,
                onChatClicked = onChatClicked
            )
        }
    }
}

@Composable
private fun TechnicianInfoRow(
    orderId: String,
    technician: Technician?,
    onChatClicked: (String, String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ProfilePhoto(
            imageUrl = technician?.user?.profilePhotoUrl,
            borderWidth = 3.dp,
            imageSize = 60.dp
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(3.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = technician?.user?.username ?: "Unassigned",
                style = TextStyle(
                    color = Color(0xFF000000),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 20.sp
                )
            )
            Text(
                text = technician?.serviceCategory.specialization,
                style = TextStyle(
                    color = Color(0xFFFF4A08),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 20.sp
                )
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        ChatImage(
            orderId = orderId,
            technicianId = technician?.user?.id,
            onChatClicked = onChatClicked
        )
        PhoneImage(technician?.user?.phoneNumber)

    }
}

@Composable
fun ChatImage(orderId: String, technicianId: String?, onChatClicked: (String, String) -> Unit) {
    Box(
        modifier = Modifier
            .size(45.dp)
            .background(color = Color(0xFFFF4400), shape = CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    if (technicianId != null) {
                        onChatClicked(orderId, technicianId)
                    }
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painterResource(R.drawable.chat_icon),
            contentDescription = "Chat Icon",
        )
    }
}

@Composable
fun PhoneImage(phoneNumber: String?) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .size(45.dp)
            .background(color = Color(0xFFFF4400), shape = CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    context.makePhoneCall(phoneNumber.toString())
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painterResource(R.drawable.phone_icon),
            contentDescription = "Phone Icon",
        )
    }
}

val ServiceCategory?.specialization: String
    get() = when (this) {
        ServiceCategory.CARPENTRY -> "Carpenter"
        ServiceCategory.PAINTING -> "Painter"
        ServiceCategory.CONDITIONING -> "HVAC Technician"
        ServiceCategory.PLUMBING -> "Plumber"
        ServiceCategory.ELECTRICITY -> "Electrician"
        else -> "Technician"
    }
