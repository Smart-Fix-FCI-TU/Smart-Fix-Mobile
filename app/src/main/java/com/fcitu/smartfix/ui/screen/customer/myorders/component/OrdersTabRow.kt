package com.fcitu.smartfix.ui.screen.customer.myorders.component

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
import com.fcitu.smartfix.ui.screen.customer.myorders.OrdersTab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersTabRow(
    selectedTab: OrdersTab,
    onTabClicked: (OrdersTab) -> Unit,
    modifier: Modifier = Modifier
) {
    SecondaryTabRow(
        selectedTabIndex = selectedTab.ordinal,
        indicator = {
            TabRowDefaults.SecondaryIndicator(
                height = 4.dp,
                color = Color(0xFFFF4806),
            )
        }
    ) {
        OrdersTab.entries.forEachIndexed { index, selectedOrdersTab ->
            Tab(
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.Gray,
                selected = selectedOrdersTab.ordinal == index,
                onClick = {
                    onTabClicked(selectedOrdersTab)
                },
                text = {
                    if ((selectedOrdersTab.ordinal == index)) {
                        Text(
                            selectedOrdersTab.name,
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                lineHeight = 20.sp
                            )
                        )
                    }else{
                        Text(
                            selectedOrdersTab.name,
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                lineHeight = 20.sp
                            )
                        )
                    }

                }
            )
        }
    }

}

@Preview(showSystemUi = true)
@Composable
private fun Test(
) {
    OrdersTabRow(OrdersTab.ACTIVE, {})
}
