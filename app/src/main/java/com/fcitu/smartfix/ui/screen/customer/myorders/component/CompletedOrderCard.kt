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
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fcitu.smartfix.R
import com.fcitu.smartfix.domain.entity.Address
import com.fcitu.smartfix.domain.entity.Order
import com.fcitu.smartfix.domain.entity.Technician
import com.fcitu.smartfix.domain.entity.User
import com.fcitu.smartfix.domain.model.OrderStatus
import com.fcitu.smartfix.domain.model.ServiceCategory
import com.fcitu.smartfix.domain.model.UserRole
import com.fcitu.smartfix.ui.designSystem.components.text.Text
import com.fcitu.smartfix.ui.screen.customer.home.component.ProfilePhoto
import com.fcitu.smartfix.ui.utils.toOrderTimeFormat
import kotlinx.datetime.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Composable
fun CompletedOrderCard(
    order: Order,
    technician: Technician? = null,
    onCompletedOrderClicked: () -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .fillMaxWidth()
            .background(color = Color.White, shape = RoundedCornerShape(32.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onCompletedOrderClicked
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
            TechnicianInfoRow(technician = technician)
        }
    }
}


@Composable
private fun TechnicianInfoRow(technician: Technician?) {
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
        CompletedBadge()

    }
}

@Composable
fun CompletedBadge(modifier: Modifier = Modifier) {
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
                tint = Color(0xFF34C759)
            )
            Text(
                "Completed",
                style = TextStyle(
                    color = Color(0xFF34C759),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    lineHeight = 16.sp
                )
            )
        }
    }
}

@OptIn(ExperimentalUuidApi::class)
@Preview(showBackground = true)
@Composable
private fun Test(

) {
    CompletedOrderCard(
        order = Order(
            id = Uuid.random().toString(),
            customer = Order.UserInfo(id = Uuid.random().toString(), name = "Fouad"),
            technician = Order.UserInfo(id = Uuid.random().toString(), name = "Ahmed Mohamed"),
            details = Order.OrderDetails(
                serviceCategory = ServiceCategory.ELECTRICITY,
                title = "change lamb",
                "the lamb is broken", problemPhotoUrls = emptyList(),
                address = Address(
                    id = Uuid.random().toString(),
                    fullAddress = "Tanta",
                    location = Address.Location(40.0, 41.0),
                    floor = "2",
                    apartmentNo = "3"
                ), additionalNotes = ""
            ),
            repairPhotos = Order.RepairPhotos(
                beforeRepairUrls = emptyList(),
                afterRepairUrls = emptyList()
            ),
            status = OrderStatus.ON_WAY, timeline = Order.OrderTimeline(
                createdAt = LocalDateTime(2024, 1, 15, 10, 30),
                acceptedAt = LocalDateTime(2024, 1, 15, 11, 0),
                arrivedAt = LocalDateTime(2024, 1, 15, 12, 0),
                startedAt = LocalDateTime(2024, 1, 15, 12, 30),
                completedAt = LocalDateTime(2024, 1, 15, 12, 30),
            )
        ), technician = Technician(
            user = User(
                id = "5425425",
                phoneNumber = "563767567262",
                firstName = "Fouad",
                lastName = "Elmeligy",
                username = "Fouad Elmeligy",
                birthOfDate = "2/2/2002",
                nationalId = "25362627246",
                email = "fouad@gmail.com",
                role = UserRole.CUSTOMER,
                profilePhotoUrl = " ",
                address = Address(
                    id = "523455",
                    fullAddress = "Tanta",
                    location = Address.Location(30.0, 31.0),
                    floor = "1",
                    apartmentNo = "2"
                )
            ),
            serviceCategory = ServiceCategory.ELECTRICITY,
            isAvailable = true,
            isOnJob = false,
            yearsOfExperience = 5,
            bio = "",
            averageRating = 3.5F,
            reviewCount = 4,
            reviews = emptyList(),
        ), {})
}