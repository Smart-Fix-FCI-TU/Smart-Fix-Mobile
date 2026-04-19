package com.fcitu.smartfix.ui.theme.designSystem.components.switches

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Switch(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    onContainerColor: Color = White,
    onContentColor: Color = Color(0xFFFFFFFF),
    onDisabledContainerColor: Color = Color(0xFF111D2E),
    onDisabledContentColor: Color = White,
    offContainerColor: Color = Color(0xFFEAECF0),
    offContentColor: Color = White.copy(alpha = 0.6f),
    offDisabledContainerColor: Color = Color(0xFFBEC0CC),
    offDisabledContentColor: Color = White.copy(alpha = 0.6f),
) {
    val transition = updateTransition(isChecked)
    val containerColor by transition.animateColor(
        targetValueByState = { checked ->
            if (checked) onContainerColor else offContainerColor
        }
    )
    val contentColor by transition.animateColor(
        targetValueByState = { checked ->
            if (checked) onContentColor else offContentColor
        }
    )
    val disabledContainerColor =
        if (isChecked) onDisabledContainerColor else offDisabledContainerColor
    val disabledContentColor = if (isChecked) onDisabledContentColor else offDisabledContentColor
    val contentAlignment = transition.animateFloat {
        if (isChecked) 1f else -1f
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (isEnabled) containerColor else disabledContainerColor)
            .width(48.dp)
            .then(
                if (!isEnabled) Modifier.alpha(0.5f)
                else Modifier.clickable {
                    onCheckedChange(!isChecked)
                }
            )
    ) {
        Box(
            modifier = Modifier
                .align(BiasAlignment(contentAlignment.value, 0f))
                .padding(4.dp)
                .clip(RoundedCornerShape(100.dp))
                .background(if (isEnabled) contentColor else disabledContentColor)
                .size(20.dp)

        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF2F4F7)
@Composable
private fun PreviewSwitch() {
    val (isSwitchChecked, onCheckedChange) = remember { mutableStateOf(false) }

    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Switch(
            isChecked = isSwitchChecked,
            onCheckedChange = onCheckedChange
        )

        Switch(
            isChecked = false,
            isEnabled = false,
            onCheckedChange = onCheckedChange
        )

        Switch(
            isChecked = true,
            onCheckedChange = onCheckedChange
        )

        Switch(
            isChecked = true,
            isEnabled = false,
            onCheckedChange = onCheckedChange
        )
    }
}