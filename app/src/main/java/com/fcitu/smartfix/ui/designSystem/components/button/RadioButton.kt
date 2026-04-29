package com.fcitu.smartfix.ui.designSystem.components.button

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fcitu.smartfix.ui.designSystem.theme.Cairo

@Composable
fun RadioButton(
    isSelected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    label: String? = null,
    shape: Shape = RoundedCornerShape(100.dp),
    isEnabled: Boolean = true
) {

    val animatedBorderDp by animateDpAsState(
        targetValue = if (isSelected) 6.dp else 1.dp
    )

    val animatedSelectionBorderColor by animateColorAsState(
        targetValue = if (isSelected) Color(0xFF000000) else Color(0xFFEAECF0)
    )

    val animatedDisabledBorderColor by animateColorAsState(
        targetValue = if (isSelected) Color(0xFFBEC0CC) else Color(0xFFEAECF0)
    )

    val animatedBorderColor by animateColorAsState(
        targetValue = if (isEnabled) animatedSelectionBorderColor else animatedDisabledBorderColor
    )

    val animatedUnselectedContentColor by animateColorAsState(
        targetValue = if (isSelected || !isEnabled) Color.Unspecified else Color(0xFFFFFFFF)
    )

    val animatedUnselectedLabelColor by animateColorAsState(
        targetValue = if (isSelected)
            Color(0xFF0E1017) else Color(0xFF818599)
    )

    val animatedLabelColor by animateColorAsState(
        targetValue = if (isEnabled)
            animatedUnselectedLabelColor else Color(0xFFEAECF0)
    )

    val clickableModifier = onClick?.let {
        Modifier.clickable(
            enabled = isEnabled,
            indication = null,
            interactionSource = remember { MutableInteractionSource() },
            role = Role.RadioButton
        ) {
            onClick()
        }
    } ?: Modifier

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
    ) {
        Box(
            modifier = Modifier
                .size(18.dp)
                .background(animatedUnselectedContentColor, shape)
                .border(
                    width = animatedBorderDp,
                    color = animatedBorderColor,
                    shape = shape
                )
                .clip(shape)
                .then(clickableModifier),
        )

        label?.let { text ->
            Text(
                text = text,
                color = animatedLabelColor,
                style = TextStyle(
                    fontFamily = Cairo,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    lineHeight = 22.sp
                )
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun RadioButtonPreview() {
    var selected by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .size(180.dp)
            .background(Color(0xFFFF2F4F7)),
        contentAlignment = Alignment.Center
    ) {
        RadioButton(
            isSelected = selected,
            label = "Label",
            isEnabled = false,
            onClick = {
                selected = !selected
            }
        )
    }
}