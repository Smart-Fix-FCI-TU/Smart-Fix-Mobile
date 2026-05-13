package com.fcitu.smartfix.ui.screen.customer.profile.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fcitu.smartfix.R
import com.fcitu.smartfix.ui.designSystem.components.text.Text
import com.fcitu.smartfix.ui.designSystem.theme.Cairo

@Composable
fun AccountInformationSection(
    phoneNumber: String,
    email: String,
    address: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Account Information",
            style = TextStyle(
                fontFamily = Cairo,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        InfoRow(icon = R.drawable.ic_phone, text = phoneNumber)
        Spacer(modifier = Modifier.height(12.dp))
        InfoRow(icon = R.drawable.ic_gmail, text = email)
        Spacer(modifier = Modifier.height(12.dp))
        InfoRow(icon = R.drawable.ic_profile, text = address) // Assuming ic_profile for address if ic_location not found
    }
}

@Composable
private fun InfoRow(icon: Int, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = Color(0xFFFF4400)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            style = TextStyle(
                fontFamily = Cairo,
                fontSize = 16.sp,
                color = Color.Black
            )
        )
    }
}
