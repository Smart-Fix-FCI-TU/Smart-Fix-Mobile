package com.fcitu.smartfix.ui.screen.technician.home.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fcitu.smartfix.ui.designSystem.components.switches.Switch
import com.fcitu.smartfix.ui.designSystem.components.text.Text
import com.fcitu.smartfix.ui.designSystem.theme.Cairo

@Composable
fun AvailabilityToggleCard(
    isAvailable: Boolean,
    canToggle: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isAvailable) Color(0xFFFF4501) else Color(0xFFBEC0CC),
        animationSpec = tween(durationMillis = 300),
        label = "availabilityBg",
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor, RoundedCornerShape(20.dp))
            .padding(horizontal = 20.dp, vertical = 16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = "I'm Available",
                    style = TextStyle(
                        fontFamily = Cairo,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.White ,
                    ),
                )
                Text(
                    text = if (!canToggle)
                        "Complete your current job first"
                    else
                        "Tap to change status",
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.8f),
                    ),
                )
            }


                Switch(isChecked = isAvailable,
                    onCheckedChange = {newValue->
                        if (canToggle) onToggle(newValue)
                    },
                    isEnabled = canToggle,
                    onContentColor = Color(0xFFFF4501),
                    onContainerColor = Color.White,
                    offContainerColor = Color.White,
                    offContentColor = Color(0xFF818599),
                    onDisabledContainerColor = Color.White,
                    onDisabledContentColor = Color(0xFFFF4501),
                    offDisabledContentColor = Color(0xFFE7E7E7),
                    offDisabledContainerColor = Color.White


                    )

        }
    }
}