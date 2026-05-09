package com.fcitu.smartfix.ui.screen.customer.booking.components

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.fcitu.smartfix.R
import com.fcitu.smartfix.ui.designSystem.components.appBar.AppBar

@Composable
fun BookingTopBar(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppBar(
        title = "Problem Description",
        leadingContent = {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = "Back"
            )
        },
        onLeadingClick = onNavigateBack,
        modifier = modifier
    )
}