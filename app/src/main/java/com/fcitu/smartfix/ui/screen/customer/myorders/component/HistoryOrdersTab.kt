package com.fcitu.smartfix.ui.screen.customer.myorders.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fcitu.smartfix.ui.screen.customer.myorders.MyOrdersInteractionListener
import com.fcitu.smartfix.ui.screen.customer.myorders.MyOrdersUiState

@Composable
fun HistoryOrdersTab(
    state: MyOrdersUiState,
    listener: MyOrdersInteractionListener,
) {
    when {
        state.isLoading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 3.dp,
                    trackColor = Color(0xFFFF4A08)
                )
            }
        }

        !state.hasHistoryOrders -> {
            OrdersEmptyState(
                "No Past Orders", "All successfully completed orders will appear here."
            )
        }

        state.hasHistoryOrders -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(items = state.historyOrders, key = { it.id }) {completedOrder ->
                    CompletedOrderCard(
                        order = completedOrder,
                        technician = state.technicianMap[completedOrder.id],
                        onCompletedOrderClicked = {},
                    )
                }
            }
        }

    }
}