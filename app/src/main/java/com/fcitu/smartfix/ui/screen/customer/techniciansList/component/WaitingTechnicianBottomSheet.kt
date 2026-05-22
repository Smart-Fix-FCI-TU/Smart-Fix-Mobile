package com.fcitu.smartfix.ui.screen.customer.techniciansList.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fcitu.smartfix.domain.entity.Technician
import com.fcitu.smartfix.ui.designSystem.components.button.PrimaryButton
import com.fcitu.smartfix.ui.designSystem.components.indicator.DotsProgressIndicator
import com.fcitu.smartfix.ui.designSystem.components.text.Text
import com.fcitu.smartfix.ui.designSystem.theme.Cairo

@Composable
fun WaitingTechnicianBottomSheet(
    technician: Technician,
    countdownTime: String,
    onClickCancelOrder: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFF2F4F7)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Waiting for Technician",
            style = TextStyle(
                fontFamily = Cairo,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1C1B1F)
            ),
            textAlign = TextAlign.Center
        )
        Text(
            text = "He's reviewing your request right now",
            style = TextStyle(
                fontFamily = Cairo,
                fontSize = 14.sp,
                color = Color(0xFF6B7280)
            ),
            textAlign = TextAlign.Center
        )

        Box(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .size(120.dp)
                .border(8.dp, Color(0xFFFF4400), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = countdownTime,
                style = TextStyle(
                    fontFamily = Cairo,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF4400)
                )
            )
        }



        DotsProgressIndicator(
            dotSize = 8.dp,
            spaceBetween = 8.dp,
            colors = listOf(
                Color(0xFFFF4400),
                Color(0xFFDA4135),
                Color(0xFFDE6767),
                Color(0xFFEAECF0),
            ),
            modifier = Modifier.padding(vertical = 16.dp)
        )

        TechnicianInfoSection(
            name = "${technician.user.firstName} ${technician.user.lastName}",
            jobTitle = technician.serviceCategory
                .name
                .lowercase()
                .replaceFirstChar { it.uppercase() },
            rating = technician.averageRating,
            distance = 1.2f,
            profilePhotoUrl = "https://images.pexels.com/videos/7055325/nature-natures-beauty-pine-plants-7055325.jpeg?auto=compress&cs=tinysrgb&w=600&loading=lazy",
            modifier = Modifier.padding(bottom = 8.dp)
        )

        PrimaryButton(
            text = "Cancel order",
            onClick = onClickCancelOrder,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            containerColor = Color(0xFFFF4400)
        )

        Spacer(modifier = Modifier.size(16.dp))
    }
}