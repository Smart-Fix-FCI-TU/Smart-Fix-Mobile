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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
fun CompletedJobCard(
    order: Order,
    user: User? = null,
    onCompletedOrderClicked: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 3.dp, shape = RoundedCornerShape(32.dp))
            .background(color = Color.White, shape = RoundedCornerShape(32.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onCompletedOrderClicked
            )
            .padding(12.dp)
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

            //  User Info Row
            CustomerInfoRow(user = user)
        }
    }
}


@Composable
private fun CustomerInfoRow(user: User?) {
    Row(
        modifier = Modifier.fillMaxWidth(),
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
                modifier = Modifier.weight(1f),
                text = user?.username ?: "Unassigned",
                style = TextStyle(
                    fontFamily = Cairo,
                    color = Color(0xFF000000),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )


            Spacer(modifier = Modifier.weight(0.5f))
            CompletedBadge()

        }
    }
}

@Composable
fun CompletedBadge() {
    Box(
        modifier = Modifier
            .background(
                color = Color(0xFF34C759).copy(alpha = 0.2f),
                shape = RoundedCornerShape(percent = 50)
            )
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = "Check Icon",
                tint = Color(0xFF34C759),
                modifier = Modifier.size(18.dp)
            )
            Text(
                "Completed",
                style = TextStyle(
                    fontFamily = Cairo,
                    color = Color(0xFF34C759),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                )
            )
        }
    }
}