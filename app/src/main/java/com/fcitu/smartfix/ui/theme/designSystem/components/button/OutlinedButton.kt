package com.fcitu.smartfix.ui.theme.designSystem.components.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fcitu.smartfix.R
import com.fcitu.smartfix.ui.theme.designSystem.components.button.content.BaseButtonContent
import sv.lib.squircleshape.SquircleShape

@Composable
fun OutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    trailingIcon: Painter? = null,
    iconSize: Dp = 20.dp,
    contentDescription: String? = null,
    iconStartPadding: Dp = 8.dp,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    contentColor: Color = Color(0xFF000000),
    disabledContentColor: Color = Color(0xFF818599),
    contentPadding: PaddingValues = PaddingValues(
        horizontal = 16.dp,
        vertical = 13.dp
    ),
    shape: Shape = SquircleShape(12.dp)
) {
    Button(
        isEnabled = isEnabled,
        shape = shape,
        borderStroke = BorderStroke(width = 1.dp, color = Color(0xFFEAECF0)),
        contentColor = contentColor,
        containerColor = Color.Transparent,
        disabledContentColor = disabledContentColor,
        disabledContainerColor = Color.Transparent,
        contentPadding = contentPadding,
        onClick = onClick,
        isLoading = isLoading,
        loadingColors = listOf(
            Color(0xFFEAECF0),
            Color(0xFF818599),
            Color(0xFF000000)
        ),
        modifier = modifier
    ) {
        BaseButtonContent(
            text = text,
            trailingIcon = trailingIcon,
            iconSize = iconSize,
            iconStartPadding = iconStartPadding,
            contentDescription = contentDescription,
            contentColor = it
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OutlinedButtonPreview() {
    OutlinedButton(
        text = "Button",
        trailingIcon = painterResource(R.drawable.ic_test),
        onClick = {},
        modifier = Modifier
    )
    OutlinedButton(
        text = "Button",
        isLoading = true,
        trailingIcon = painterResource(R.drawable.ic_test),
        onClick = {},
        modifier = Modifier
    )
    OutlinedButton(
        text = "Button",
        trailingIcon = painterResource(R.drawable.ic_test),
        onClick = {},
        isEnabled = false,
        modifier = Modifier
    )
}