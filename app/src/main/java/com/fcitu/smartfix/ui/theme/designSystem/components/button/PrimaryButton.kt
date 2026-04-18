package com.fcitu.smartfix.ui.theme.designSystem.components.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fcitu.smartfix.R
import com.fcitu.smartfix.ui.theme.designSystem.components.button.content.BaseButtonContent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import sv.lib.squircleshape.SquircleShape

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    trailingIcon: Painter? = null,
    iconSize: Dp = 20.dp,
    iconStartPadding: Dp = 8.dp,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    containerColor: Color = Color(0xFF000000),
    disabledContainerColor: Color = Color(0xFFBEC0CC),
    contentColor: Color = Color(0xFFFFFFFF),
    disabledContentColor: Color = Color(0xFF818599),
    contentPadding: PaddingValues = PaddingValues(
        horizontal = 16.dp,
        vertical = 13.dp
    ),
    shape: Shape = SquircleShape(12.dp)
) {
    Button(
        isEnabled = isEnabled,
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContentColor = disabledContentColor,
        disabledContainerColor = disabledContainerColor,
        contentPadding = contentPadding,
        shape = shape,
        isLoading = isLoading,
        loadingColors = listOf(
            White.copy(alpha = 0.38f),
            White.copy(alpha = 0.6f),
            Color(0xFFFFFFFF)
        ),
        onClick = onClick,
        modifier = modifier
    ) {
        BaseButtonContent(
            text = text,
            contentColor = it,
            trailingIcon = trailingIcon,
            iconSize = iconSize,
            iconStartPadding = iconStartPadding
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PrimaryButtonPreview() {
    var primaryButtonLoading by remember { mutableStateOf(false) }

    LaunchedEffect(primaryButtonLoading) {
        if (primaryButtonLoading) {
            launch {
                delay(1000)
                primaryButtonLoading = false
            }
        }
    }

    PrimaryButton(
        text = "Click me to test loading",
        isLoading = primaryButtonLoading,
        trailingIcon = painterResource(R.drawable.ic_test),
        onClick = { primaryButtonLoading = !primaryButtonLoading },
    )
}

@Preview(showBackground = true)
@Composable
private fun PrimaryButtonPreview2() {
    PrimaryButton(
        text = "Button",
        trailingIcon = painterResource(R.drawable.ic_test),
        onClick = {},
        modifier = Modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun PrimaryButtonPreview3() {
    PrimaryButton(
        text = "Button",
        trailingIcon = painterResource(R.drawable.ic_test),
        onClick = {},
        isEnabled = false,
        modifier = Modifier
    )
}