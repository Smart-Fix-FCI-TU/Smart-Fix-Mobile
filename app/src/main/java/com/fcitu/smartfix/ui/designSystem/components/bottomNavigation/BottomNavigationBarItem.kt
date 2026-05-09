package com.fcitu.smartfix.ui.designSystem.components.bottomNavigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fcitu.smartfix.ui.designSystem.components.text.Text
import com.fcitu.smartfix.ui.designSystem.theme.Cairo

@Composable
fun BottomNavigationBarItem(
    isSelected: Boolean,
    unselectedIcon: Painter,
    selectedIcon: Painter,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val painter = if (isSelected) selectedIcon else unselectedIcon
    val animatedIconTint by animateColorAsState(
        targetValue = if (isSelected) Color(0xFFFF4400) else Color(0xFF000000),
    )
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .height(76.dp)
            .then(
                if (isSelected) Modifier
                else Modifier.clickable(
                    onClick = onClick,
                    indication = null,
                    interactionSource = interactionSource
                )
            )
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(
                    alignment = Alignment.TopCenter
                )
        ) {
            Icon(
                painter = painter,
                modifier = Modifier.size(24.dp),
                contentDescription = title,
                tint = animatedIconTint
            )

            if (isSelected) {
                Text(
                    text = title,
                    style = TextStyle(
                        fontFamily = Cairo,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        lineHeight = 22.sp
                    ),
                    color = Color(0xFF111D2E),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}