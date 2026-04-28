package com.fcitu.smartfix.ui.theme.designSystem.components.chip

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fcitu.smartfix.R
import com.fcitu.smartfix.ui.theme.Cairo
import com.fcitu.smartfix.ui.theme.designSystem.components.text.Text

@Composable
fun Chip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    painter: Painter? = null,
    iconSize: Dp = 16.dp,
    isEnabled: Boolean = true,
    containerColor: Color = Color.Transparent,
    disabledContainerColor: Color = Color.Transparent,
    contentColor: Color = Color.Transparent,
    disabledContentColor: Color = Color.Transparent,
    shape: Shape = RoundedCornerShape(100.dp)
) {
    val transition = updateTransition(isSelected)
    val containerColor by transition.animateColor(
        targetValueByState = { isCurrentSelected ->
            if (isCurrentSelected) containerColor
            else disabledContainerColor
        }
    )
    val contentColor by transition.animateColor(
        targetValueByState = { isCurrentSelected ->
            if (isCurrentSelected) contentColor
            else disabledContentColor
        }
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(shape)
            .then(
                if (isEnabled) Modifier.clickable(onClick = onClick)
                else Modifier
            )
            .background(if (isEnabled) containerColor else Color(0xFFBEC0CC))
            .padding(
                vertical = 8.dp,
                horizontal = 12.dp
            )
    ) {

        painter?.let { iconPainter ->
            Icon(
                painter = iconPainter,
                modifier = Modifier.size(iconSize),
                contentDescription = null,
                tint = if (isEnabled) contentColor else Color(0xFF818599)
            )
        }

        Text(
            text = text,
            style = TextStyle(
                fontFamily = Cairo,
                fontWeight = FontWeight.Medium,
                fontSize = 10.sp,
                lineHeight = 16.sp
            ),
            color = if (isEnabled) contentColor else Color(0xFF818599),
            fontSize = 10.sp,
            letterSpacing = 0.sp,
            lineHeight = 16.sp
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF2F4F7)
@Composable
private fun ChipPreview() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Chip(
            text = "Chips",
            painter = painterResource(R.drawable.ic_test),
            isSelected = false,
            onClick = {}
        )

        Chip(
            text = "Chips",
            painter = painterResource(R.drawable.ic_test),
            isSelected = true,
            onClick = {}
        )

        Chip(
            text = "Chips",
            painter = painterResource(R.drawable.ic_test),
            isSelected = true,
            isEnabled = false,
            onClick = {}
        )
    }
}