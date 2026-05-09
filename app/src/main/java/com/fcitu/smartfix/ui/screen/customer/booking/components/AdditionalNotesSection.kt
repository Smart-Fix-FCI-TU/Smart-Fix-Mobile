package com.fcitu.smartfix.ui.screen.customer.booking.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fcitu.smartfix.ui.designSystem.components.text.Text
import com.fcitu.smartfix.ui.designSystem.components.textField.MultiLineTextField
import com.fcitu.smartfix.ui.designSystem.theme.Cairo


@Composable
fun AdditionalNotesSection(
    additionalNotes: String,
    onAdditionalNotesChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Additional Notes",
            style = TextStyle(
                fontFamily = Cairo,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        MultiLineTextField(
            value = additionalNotes,
            onValueChanged = onAdditionalNotesChanged,
            title = "",
            hint = "Any additional info you'd like to tell us...",
            modifier = Modifier.fillMaxWidth()
        )
    }
}