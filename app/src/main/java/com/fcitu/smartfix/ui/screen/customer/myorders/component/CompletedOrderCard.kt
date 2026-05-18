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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fcitu.smartfix.R
import com.fcitu.smartfix.domain.entity.Order
import com.fcitu.smartfix.domain.entity.Technician
import com.fcitu.smartfix.ui.designSystem.components.text.Text
import com.fcitu.smartfix.ui.designSystem.theme.Cairo
import com.fcitu.smartfix.ui.screen.customer.home.component.ProfilePhoto
import com.fcitu.smartfix.ui.utils.toOrderTimeFormat

@Composable
fun CompletedOrderCard(
    order: Order,
    technician: Technician? = null,
    onCompletedOrderClicked: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
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

            //  Technician Info Row
            TechnicianInfoRow(technician = technician)
        }
    }
}


@Composable
private fun TechnicianInfoRow(technician: Technician?) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ProfilePhoto(
            imageUrl = technician?.user?.profilePhotoUrl,
            borderWidth = 3.dp,
            imageSize = 60.dp
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = technician?.user?.username ?: "Unassigned",
                style = TextStyle(
                    fontFamily = Cairo,
                    color = Color(0xFF000000),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                ),
                maxLines = 1
            )
            Text(
                text = technician?.serviceCategory.specialization,
                style = TextStyle(
                    fontFamily = Cairo,
                    color = Color(0xFFFF4A08),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                )
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        CompletedBadge()

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