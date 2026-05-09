package com.fcitu.smartfix.ui.screen.customer.booking.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fcitu.smartfix.ui.designSystem.components.text.Text
import com.fcitu.smartfix.ui.designSystem.components.textField.MultiLineTextField
import com.fcitu.smartfix.ui.designSystem.components.textField.TextField
import com.fcitu.smartfix.ui.designSystem.theme.Cairo

@Composable
fun ProblemDescriptionSection(
    shortTitle: String,
    detailedDescription: String,
    onShortTitleChanged: (String) -> Unit,
    onDetailedDescriptionChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Short Title
        TextField(
            value = shortTitle,
            onValueChanged = onShortTitleChanged,
            title = "Short Title",
            hint = "Example: Electrical circuit repair",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Detailed Description
        MultiLineTextField(
            value = detailedDescription,
            onValueChanged = onDetailedDescriptionChanged,
            title = "Detailed Description",
            hint = "Describe the problem with as much detail as possible...",
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "${detailedDescription.length} / 500",
            style = TextStyle(
                fontFamily = Cairo,
                fontSize = 11.sp,
                color = Color.Gray
            ),
            modifier = Modifier.align(Alignment.End)
        )
    }
}