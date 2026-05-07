package com.fcitu.smartfix.ui.screen.customer.home.component

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fcitu.smartfix.domain.entity.ServiceItem
import com.fcitu.smartfix.domain.model.ServiceCategory
import com.fcitu.smartfix.ui.designSystem.components.text.Text

@Composable
fun ServiceChip(
    serviceItem: ServiceItem,
    isSelected: Boolean=false,
    onItemClick: (ServiceCategory) -> Unit
) {
    val bgColor = if (isSelected) Color(0xFFFF4806) else Color(0xFFE7E7E7)
    val icon = if (isSelected) serviceItem.activeIcon else serviceItem.inactiveIcon
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.08f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "chipScale"
    )

    val testStyle = if (isSelected) TextStyle(
        color = Color(0xFF1C1B1F),
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        lineHeight = 16.sp
    )
    else TextStyle(
        color = Color(0xFF1C1B1F),
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center, modifier = Modifier
                .size(64.dp)
                .scale(scale)
                .clip(
                    RoundedCornerShape(16.dp)
                )
                .background(color = bgColor)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {onItemClick(serviceItem.serviceCategory)}
                )
        ) {
            Image(painter = painterResource(icon), contentDescription = "Service Icon")
        }
        Text(text = serviceItem.serviceName, style = testStyle)
    }
}
