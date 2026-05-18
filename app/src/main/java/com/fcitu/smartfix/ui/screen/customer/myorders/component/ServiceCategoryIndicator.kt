package com.fcitu.smartfix.ui.screen.customer.myorders.component


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.fcitu.smartfix.R
import com.fcitu.smartfix.domain.model.ServiceCategory

@Composable
fun ServiceCategoryIndicator(
    serviceCategory: ServiceCategory,
    modifier: Modifier = Modifier
){
    val iconRes = when (serviceCategory) {
        ServiceCategory.PLUMBING -> R.drawable.plumbing_icon
        ServiceCategory.ELECTRICITY -> R.drawable.electricity_icon
        ServiceCategory.CONDITIONING -> R.drawable.conditioning_icon
        ServiceCategory.PAINTING -> R.drawable.painting_icon
        ServiceCategory.CARPENTRY -> R.drawable.carpentry_icon
    }

    Box(
        modifier = modifier
            .size(48.dp)
            .background(Color(0xFFE7E7E7), RoundedCornerShape(16.dp))
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = serviceCategory.name,
            modifier = Modifier.size(24.dp)
        )
    }
}