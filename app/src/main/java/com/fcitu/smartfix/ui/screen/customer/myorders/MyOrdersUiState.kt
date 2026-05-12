package com.fcitu.smartfix.ui.screen.customer.myorders

import com.fcitu.smartfix.domain.entity.Order
import com.fcitu.smartfix.domain.entity.Technician

data class MyOrdersUiState(
    val activeOrders: List<Order> = emptyList(),
    val historyOrders: List<Order> = emptyList(),
    val selectedTab: OrdersTab = OrdersTab.ACTIVE,
    val technicianMap: Map<String, Technician> = emptyMap(),
    val isLoadingActive: Boolean = false,
    val isLoadingHistory: Boolean = false,
    val hasNetworkConnection: Boolean = true,
    val hasLoaded: Boolean = false,
    val error: String? = null,
) {
    val hasActiveOrders: Boolean get() = activeOrders.isNotEmpty()
    val hasHistoryOrders: Boolean get() = historyOrders.isNotEmpty()

}

enum class OrdersTab {
    ACTIVE,
    HISTORY,
}