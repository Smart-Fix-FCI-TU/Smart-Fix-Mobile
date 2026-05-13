package com.fcitu.smartfix.ui.screen.technician.profile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TechnicianStatsSection(
    rating: String,
    reviewCount: String,
    experience: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatItem(label = "RATING", value = rating, valueColor = Color(0xFFFF4400))
        Box(modifier = Modifier.width(1.dp).height(40.dp).background(Color(0xFFEEEEEE)))
        StatItem(label = "REVIEWS", value = reviewCount)
        Box(modifier = Modifier.width(1.dp).height(40.dp).background(Color(0xFFEEEEEE)))
        StatItem(label = "EXPERIENCE", value = experience)
    }
}
