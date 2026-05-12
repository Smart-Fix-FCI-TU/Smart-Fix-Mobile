@file:OptIn(ExperimentalMaterial3Api::class)

package com.fcitu.smartfix.ui.screen.customer.myorders.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fcitu.smartfix.ui.screen.customer.myorders.OrdersTab

@Composable
fun OrdersTabRow(
    selectedTab: OrdersTab,
    onTabClicked: (OrdersTab) -> Unit
) {
    SecondaryTabRow(
        selectedTabIndex = selectedTab.ordinal,
        modifier = Modifier.fillMaxWidth(),
        divider = {},
        indicator = {
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier
                    .tabIndicatorOffset(
                        selectedTabIndex = selectedTab.ordinal,
                    )
                    .padding(horizontal = 16.dp),
                height = 4.dp,
                color = Color(0xFFFF4806),
            )
        }
    ) {
        OrdersTab.entries.forEachIndexed { index, selectedOrdersTab ->
            val isSelected = selectedOrdersTab.ordinal == index
            Tab(
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.Gray,
                selected = isSelected,
                onClick = {
                    onTabClicked(selectedOrdersTab)
                },
                text = {
                    androidx.compose.material3.Text(
                        selectedOrdersTab.name, style =
                            TextStyle(
                                color = Color.Black,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                lineHeight = 20.sp
                            )

                    )

                }
            )
        }
    }

}