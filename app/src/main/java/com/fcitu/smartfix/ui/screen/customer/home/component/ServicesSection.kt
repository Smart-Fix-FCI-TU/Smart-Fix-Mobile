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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.fcitu.smartfix.ui.designSystem.components.button.Button
import com.fcitu.smartfix.ui.designSystem.components.text.Text
import com.fcitu.smartfix.ui.screen.customer.home.HomeInteractionListener
import com.fcitu.smartfix.ui.screen.customer.home.HomeUiState

@Composable
fun ServicesSection(
    state: HomeUiState,
    listener: HomeInteractionListener,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(vertical = 16.dp)
    ) {
        HomeSection(title = "Services", actionName = null, modifier = modifier.fillMaxWidth())
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(items = state.servicesList, key = { it.serviceCategory.name }) { serviceItem ->
                ServiceChip(
                    serviceItem = serviceItem,
                    isSelected = serviceItem.serviceCategory.name == state.selectedCategory
                ) { serviceCategory ->
                    listener.onCategorySelected(serviceCategory.name)
                }
            }
        }
        ChooseServiceButton(isServiceSelected = !state.selectedCategory.isNullOrEmpty(), modifier = modifier.padding(top = 60.dp)) {
            listener.onChooseServiceClicked()
        }
    }
}


// Service Chip For Every Service Category
@Composable
private fun ServiceChip(
    serviceItem: ServiceItem,
    isSelected: Boolean = false,
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
                    onClick = { onItemClick(serviceItem.serviceCategory) }
                )
        ) {
            Image(painter = painterResource(icon), contentDescription = "Service Icon")
        }
        Text(text = serviceItem.serviceName, style = testStyle)
    }
}


// Chose Service Button To Navigate to Booking Screen
@Composable
fun ChooseServiceButton(
    isServiceSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val buttonColor = if (isServiceSelected) Color(0xFFFF4806) else Color(0xFFBEC0CC)
    val contentColor = if (isServiceSelected) Color(0xFFFFFFFF) else Color(0xFF818599)
    Button(
        onClick = { onClick() },
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.fillMaxWidth(), // ← fillMaxWidth

        contentPadding = PaddingValues(vertical = 17.dp, horizontal = 24.dp),
        isEnabled = isServiceSelected, containerColor = buttonColor,
        contentColor = contentColor, disabledContentColor = Color(0xFFBEC0CC),
        disabledContainerColor = Color(0xFF818599)
    ) { contentColor ->
        Text(
            "Choose a Service",
            style = TextStyle(
                color = contentColor,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                lineHeight = 24.sp
            )
        )
    }
}
