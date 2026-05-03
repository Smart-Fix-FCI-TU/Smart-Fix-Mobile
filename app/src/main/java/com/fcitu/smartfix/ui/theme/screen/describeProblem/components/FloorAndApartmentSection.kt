package com.fcitu.smartfix.ui.theme.screen.describeProblem.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fcitu.smartfix.ui.theme.designSystem.components.textField.TextField

@Composable
fun FloorAndApartmentSection(
    floor: String,
    apartmentNo: String,
    onFloorChanged: (String) -> Unit,
    onApartmentNoChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextField(
            value = floor,
            onValueChanged = onFloorChanged,
            title = "Floor",
            hint = "e.g. 4",
            modifier = Modifier.weight(1f)
        )
        TextField(
            value = apartmentNo,
            onValueChanged = onApartmentNoChanged,
            title = "Apartment No.",
            hint = "e.g. 12",
            modifier = Modifier.weight(1f)
        )
    }
}