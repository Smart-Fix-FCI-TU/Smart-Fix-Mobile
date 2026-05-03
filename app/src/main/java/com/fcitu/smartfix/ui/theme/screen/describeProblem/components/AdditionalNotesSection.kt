package com.fcitu.smartfix.ui.theme.screen.describeProblem.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fcitu.smartfix.ui.theme.designSystem.components.textField.MultiLineTextField

@Composable
fun AdditionalNotesSection(
    additionalNotes: String,
    onAdditionalNotesChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Additional Notes",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
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