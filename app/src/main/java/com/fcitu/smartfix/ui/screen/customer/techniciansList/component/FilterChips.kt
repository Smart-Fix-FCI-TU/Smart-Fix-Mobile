package com.fcitu.smartfix.ui.screen.customer.techniciansList.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fcitu.smartfix.ui.designSystem.components.chip.Chip
import com.fcitu.smartfix.ui.screen.customer.techniciansList.FilterType

@Composable
fun FilterChips(
    selectedFilter: FilterType,
    onClickFilter: (FilterType) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(FilterType.entries) { filter ->
            val isSelected = (filter == selectedFilter)
            Chip(
                text = filter.name
                    .replace("_", " ")
                    .lowercase()
                    .replaceFirstChar { it.uppercase() },
                isSelected = isSelected,
                onClick = { onClickFilter(filter) },
                containerColor = Color(0xFFFF4400),
                disabledContainerColor = Color(0xFFF2F4F7),
                contentColor = Color.White,
                disabledContentColor = Color(0xFF475467)
            )
        }

    }
}