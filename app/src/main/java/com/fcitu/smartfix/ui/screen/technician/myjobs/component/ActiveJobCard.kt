package com.fcitu.smartfix.ui.screen.technician.myjobs.component


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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fcitu.smartfix.R
import com.fcitu.smartfix.domain.entity.Order
import com.fcitu.smartfix.domain.entity.User
import com.fcitu.smartfix.ui.designSystem.components.text.Text
import com.fcitu.smartfix.ui.designSystem.theme.Cairo
import com.fcitu.smartfix.ui.screen.customer.home.component.ProfilePhoto
import com.fcitu.smartfix.ui.screen.customer.myorders.component.OrderHeaderRow
import com.fcitu.smartfix.ui.utils.toOrderTimeFormat

@Composable
fun ActiveJobCard(
    activeOrder: Order,
    user: User? = null,
    onActiveJobClicked: (String) -> Unit,
    onChatClicked: (String, String) -> Unit,
    onDialerClicked: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 3.dp, shape = RoundedCornerShape(32.dp))
            .background(color = Color.White, shape = RoundedCornerShape(32.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { onActiveJobClicked(activeOrder.id) }
            )
            .padding(12.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //  Header Row: Category + Title + Time
            OrderHeaderRow(
                serviceCategory = activeOrder.details.serviceCategory,
                title = activeOrder.details.title,
                time = activeOrder.timeline.acceptedAt.toOrderTimeFormat()
            )

            // Divider
            Image(
                painter = painterResource(id = R.drawable.line),
                contentDescription = "Divider"
            )

            //  User Info Row
            UserInfoRow(
                orderId = activeOrder.id,
                user = user,
                onChatClicked = onChatClicked,
                onDialerClicked = onDialerClicked

            )
        }
    }
}

@Composable
private fun UserInfoRow(
    orderId: String,
    user: User?,
    onChatClicked: (String, String) -> Unit,
    onDialerClicked: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Row(
            modifier = Modifier.weight(2f),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfilePhoto(
                imageUrl = user?.profilePhotoUrl,
                borderWidth = 3.dp,
                imageSize = 60.dp
            )
            Text(
                text = user?.username ?: "Unassigned",
                style = TextStyle(
                    fontFamily = Cairo,
                    color = Color(0xFF000000),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 20.sp
                ),
                maxLines = 1,
            )
        }

        Spacer(modifier = Modifier.weight(0.5f))
        ChatImage(
            orderId = orderId,
            userId = user?.id,
            onChatClicked = onChatClicked
        )
        PhoneImage(user?.phoneNumber, onDialerClicked = onDialerClicked)

    }
}

@Composable
fun ChatImage(orderId: String, userId: String?, onChatClicked: (String, String) -> Unit) {
    Box(
        modifier = Modifier
            .size(45.dp)
            .background(color = Color(0xFFFF4400), shape = CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    if (userId != null) {
                        onChatClicked(orderId, userId)
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
fun PhoneImage(phoneNumber: String?, onDialerClicked: (String) -> Unit) {

    Box(
        modifier = Modifier
            .size(45.dp)
            .background(color = Color(0xFFFF4400), shape = CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    onDialerClicked(phoneNumber ?: "")
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.phone_icon),
            contentDescription = "Phone Icon",
            modifier = Modifier.size(16.dp)
        )
    }
}
