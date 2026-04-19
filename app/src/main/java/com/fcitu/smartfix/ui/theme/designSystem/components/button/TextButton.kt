package com.fcitu.smartfix.ui.theme.designSystem.components.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fcitu.smartfix.R
import com.fcitu.smartfix.ui.theme.designSystem.components.button.content.BaseButtonContent

@Composable
fun TextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    trailingIcon: Painter? = null,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    contentColor: Color = Color(0xFF000000),
    disabledContentColor: Color = Color(0xFFBEC0CC),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    iconSize: Dp = 16.dp,
    iconStartPadding: Dp = 4.dp,
    overflow: TextOverflow = TextOverflow.Ellipsis
) {
    Button(
        onClick = onClick,
        isEnabled = isEnabled,
        contentPadding = contentPadding,
        contentColor = contentColor,
        disabledContentColor = disabledContentColor,
        shape = RoundedCornerShape(2.dp),
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
            overflow = overflow,
            contentColor = it,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TextButtonPreview() {
    TextButton(
        text = "Button",
        trailingIcon = painterResource(R.drawable.ic_test),
        onClick = {},
        modifier = Modifier
    )
}