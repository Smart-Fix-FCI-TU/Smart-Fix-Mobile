package com.fcitu.smartfix.ui.screen.customer.booking.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.fcitu.smartfix.ui.designSystem.components.textField.MultiLineTextField


@Composable
fun AdditionalNotesSection(
    additionalNotes: String,
    onAdditionalNotesChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    MultiLineTextField(
        value = additionalNotes,
        onValueChanged = onAdditionalNotesChanged,
        title = "Additional Notes",
        hint = "Any additional info you'd like to tell us...",
        modifier = Modifier.fillMaxWidth()
    )
}