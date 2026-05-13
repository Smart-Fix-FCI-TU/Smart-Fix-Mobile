package com.fcitu.smartfix.ui.screen.technician.profile.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fcitu.smartfix.ui.designSystem.components.text.Text
import com.fcitu.smartfix.ui.designSystem.theme.Cairo

@Composable
fun TechnicianAboutSection(
    bio: String,
    onShowMoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "About the Technician",
            style = TextStyle(
                fontFamily = Cairo,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = bio,
            style = TextStyle(
                fontFamily = Cairo,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = Color.DarkGray
            )
        )
        Text(
            text = "Show More",
            style = TextStyle(
                fontFamily = Cairo,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color(0xFFFF4400)
            ),
            modifier = Modifier
                .clickable { onShowMoreClick() }
                .padding(vertical = 4.dp)
        )
    }
}
