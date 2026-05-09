package com.fcitu.smartfix.ui.designSystem.components.button.content

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.fcitu.smartfix.ui.designSystem.components.text.Text
import com.fcitu.smartfix.ui.designSystem.theme.Cairo

@Composable
internal fun BaseButtonContent(
    text: String?,
    trailingIcon: Painter?,
    contentColor: Color,
    iconSize: Dp,
    iconStartPadding: Dp,
    contentDescription: String? = null,
    overflow: TextOverflow = TextOverflow.Ellipsis,
) {
    text?.let {
        Text(
            text = text,
            style = TextStyle(
                fontFamily = Cairo,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 22.sp
            ),
            color = contentColor,
            overflow = overflow,
        )
    }

    trailingIcon?.let {
        Icon(
            painter = trailingIcon,
            contentDescription = contentDescription,
            modifier = Modifier
                .padding(start = iconStartPadding)
                .size(iconSize),
            tint = contentColor
        )
    }
}