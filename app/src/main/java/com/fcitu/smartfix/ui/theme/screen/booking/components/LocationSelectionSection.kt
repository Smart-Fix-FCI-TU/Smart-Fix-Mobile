package com.fcitu.smartfix.ui.theme.screen.booking.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.fcitu.smartfix.R
import com.fcitu.smartfix.ui.theme.designSystem.components.textField.TextField

@Composable
fun LocationSelectionSection(
    location: String,
    onLocationChanged: (String) -> Unit,
    onMapPlaceholderClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Location TextField
        TextField(
            value = location,
            onValueChanged = onLocationChanged,
            title = "Location",
            hint = "Enter or select location",
            readOnly = false,
            trailingIcon = painterResource(id = R.drawable.ic_location),
            trailingIconTint = Color(0xFFFFA500),
            onTrailingIconClick = onMapPlaceholderClicked,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Map Placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFE0E0E0))
                .clickable(onClick = onMapPlaceholderClicked),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_location),
                contentDescription = "Map Pin",
                tint = Color.Red,
                modifier = Modifier.size(48.dp)
            )
        }
    }
}