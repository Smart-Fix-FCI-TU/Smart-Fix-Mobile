package com.fcitu.smartfix.ui.screen.shared.orderDetails.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.fcitu.smartfix.R
import com.fcitu.smartfix.ui.designSystem.components.appBar.AppBar

@Composable
fun OrderDetailsHeader(
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppBar(
        title = "Order Details",
        onLeadingClick = onBackClicked,
        leadingContent = {
            Icon(
                painter = painterResource(R.drawable.ic_back),
                contentDescription = "Back",
                tint = Color.Black
            )
        },
        modifier = modifier
    )
}