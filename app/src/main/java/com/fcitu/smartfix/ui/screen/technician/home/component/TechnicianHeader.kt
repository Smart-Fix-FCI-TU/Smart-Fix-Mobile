package com.fcitu.smartfix.ui.screen.technician.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fcitu.smartfix.domain.entity.Technician
import com.fcitu.smartfix.ui.designSystem.components.text.Text
import com.fcitu.smartfix.ui.designSystem.theme.Cairo
import com.fcitu.smartfix.ui.screen.customer.home.component.ProfilePhoto
import com.fcitu.smartfix.ui.screen.customer.myorders.component.specialization

@Composable
fun TechnicianHeader(
    technician: Technician,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = technician.user.firstName + " " + technician.user.lastName,
                style = TextStyle(
                    fontFamily = Cairo,
                    fontWeight = FontWeight.Black,
                    fontSize = 28.sp,
                    color = Color(0xFF1C1B1F),
                ),
            )
            Text(
                text = technician.serviceCategory.specialization,
                style = TextStyle(
                    fontSize = 14.sp,
                    color = Color(0xFFFF4501),
                    fontWeight = FontWeight.Medium,
                ),
            )
            // Rating Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFFFF4501),
                    modifier = Modifier.size(16.dp),
                )
                Text(
                    text = "${technician.averageRating}",
                    style = TextStyle(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1C1B1F),
                    ),
                )
                Text(
                    text = "(${technician.reviewCount} Reviews)",
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = Color(0xFF9E9E9E),
                    ),
                )
            }
        }

        // Profile Photo with online indicator
            ProfilePhoto(
                imageUrl = technician.user.profilePhotoUrl,
                imageSize = 72.dp,
                borderWidth = 3.dp,
            )

    }
}