package com.fcitu.smartfix.ui.screen.customer.techniciansList.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.fcitu.smartfix.R
import com.fcitu.smartfix.domain.entity.Technician
import com.fcitu.smartfix.ui.designSystem.components.button.PrimaryButton
import com.fcitu.smartfix.ui.designSystem.components.text.Text
import com.fcitu.smartfix.ui.designSystem.theme.Cairo

@Composable
fun TechnicianCard(
    technician: Technician,
    onTechnicianClick: () -> Unit,
    onClickOrderNow: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        TechnicianDetails(
            technician = technician,
            onTechnicianClick = onTechnicianClick
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Available Now",
                style = TextStyle(
                    fontFamily = Cairo,
                    color = Color(0xFF16A34A),
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            PrimaryButton(
                text = "Order Now",
                onClick = onClickOrderNow,
                modifier = Modifier.padding(start = 16.dp),
                contentPadding = PaddingValues(12.dp),
                containerColor = Color(0xFFFF4400),
            )
        }
    }
}

@Composable
private fun TechnicianDetails(
    technician: Technician,
    onTechnicianClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { onTechnicianClick() }
            ),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AsyncImage(
            model = "https://images.pexels.com/videos/7055325/nature-natures-beauty-pine-plants-7055325.jpeg?auto=compress&cs=tinysrgb&w=600&loading=lazy",
            contentDescription = "Technician profile picture",
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
        TechnicianInfo(
            fullName = "${technician.user.firstName} ${technician.user.lastName}",
            jobTitle = technician.serviceCategory
                .name
                .lowercase()
                .replaceFirstChar { it.uppercase() },
            rating = technician.averageRating,
            reviewsCount = technician.reviewCount
        )
        Spacer(modifier = Modifier.weight(1f))
        LocationDistance(distance = "1.2")
    }
    Row(
        modifier = Modifier
            .padding(top = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF2F4F7))
            .padding(vertical = 4.dp, horizontal = 12.dp),
    ) {
        Text(
            text = "${technician.yearsOfExperience} Years Exp",
            style = TextStyle(
                fontFamily = Cairo,
                color = Color.Black,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}

@Composable
private fun LocationDistance(
    distance: String
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF2F4F7))
            .padding(vertical = 4.dp, horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_location),
            contentDescription = "Location",
            tint = Color(0xFFFF4502),
        )
        Text(
            text = "$distance Km",
            style = TextStyle(
                fontFamily = Cairo,
                color = Color(0xFFFF4502),
                fontSize = 12.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
private fun TechnicianInfo(
    fullName: String,
    jobTitle: String,
    rating: Float,
    reviewsCount: Int
) {
    Column {
        Text(
            text = fullName,
            style = TextStyle(
                fontFamily = Cairo,
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 24.sp
            )
        )
        Text(
            text = jobTitle,
            style = TextStyle(
                fontFamily = Cairo,
                color = Color(0xFFEA580C),
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Medium
            )
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "Rating",
                tint = Color(0xFFFF4400),
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = rating.toString(),
                style = TextStyle(
                    fontFamily = Cairo,
                    color = Color(0xFF1C1B1F),
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = "($reviewsCount reviews)",
                style = TextStyle(
                    fontFamily = Cairo,
                    color = Color(0xFF6B7280),
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Normal
                )
            )
        }
    }
}