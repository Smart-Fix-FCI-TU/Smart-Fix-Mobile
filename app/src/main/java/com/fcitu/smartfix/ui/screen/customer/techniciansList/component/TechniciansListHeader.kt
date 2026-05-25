package com.fcitu.smartfix.ui.screen.customer.techniciansList.component

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.fcitu.smartfix.R
import com.fcitu.smartfix.ui.designSystem.components.appBar.AppBar

@Composable
fun TechniciansListHeader(
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppBar(
        title = "Technicians List",
        leadingContent = {
            Icon(
                painter = painterResource(R.drawable.ic_back),
                tint = Color.Black,
                contentDescription = "Back",
                modifier = Modifier
            )
        },
        onLeadingClick = onBackClicked,
        modifier = modifier,
    )
}