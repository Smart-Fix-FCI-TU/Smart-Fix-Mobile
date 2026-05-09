package com.fcitu.smartfix.ui.designSystem.components.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
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
import sv.lib.squircleshape.SquircleShape

@Composable
fun FabButton(
    painter: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    iconSize: Dp = 24.dp,
    containerColor: Color = Color(0xFF000000),
    contentColor: Color = Color(0xFFFFFFFF),
    contentPadding: PaddingValues = PaddingValues(16.dp),
    shape: Shape = SquircleShape(12.dp)
) {
    Button(
        onClick = onClick,
        containerColor = containerColor,
        contentColor = contentColor,
        shape = shape,
        contentPadding = contentPadding,
        modifier = modifier
    ) {
        Icon(
            painter = painter,
            tint = it,
            contentDescription = contentDescription,
            modifier = Modifier.size(iconSize)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FabButtonPreview() {
    FabButton(
        painter = painterResource(R.drawable.ic_test),
        onClick = {},
        modifier = Modifier
    )
}