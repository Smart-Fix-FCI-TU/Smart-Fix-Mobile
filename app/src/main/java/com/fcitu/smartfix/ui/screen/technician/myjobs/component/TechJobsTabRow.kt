package com.fcitu.smartfix.ui.screen.technician.myjobs.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fcitu.smartfix.ui.designSystem.components.text.Text
import com.fcitu.smartfix.ui.screen.technician.myjobs.TechJobsTab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TechJobsTabRow(
    selectedTab: TechJobsTab,
    onTabClicked: (TechJobsTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    SecondaryTabRow(
        selectedTabIndex = selectedTab.ordinal,
        modifier = modifier.fillMaxWidth(),
        containerColor = Color.White,
        divider = {},
        indicator = {
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(selectedTab.ordinal),
                height = 3.dp,
                color = Color(0xFFFF4806),
            )
        },
    ) {
        TechJobsTab.entries.forEach { tab ->
            val isSelected = tab == selectedTab
            Tab(
                selected = isSelected,
                onClick = { onTabClicked(tab) },
                text = {
                    Text(
                        text = tab.name.lowercase()
                            .replaceFirstChar { it.uppercase() },
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = if (isSelected) FontWeight.Bold
                            else FontWeight.Normal,
                            color = if (isSelected) Color(0xFF1C1B1F)
                            else Color(0xFF9E9E9E),
                        ),
                    )
                },
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun Test() {
    TechJobsTabRow(TechJobsTab.ACTIVE, {})
}