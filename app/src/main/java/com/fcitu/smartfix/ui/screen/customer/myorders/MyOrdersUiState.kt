package com.fcitu.smartfix.ui.screen.customer.myorders

import com.fcitu.smartfix.domain.entity.Order

data class MyOrdersUiState(
    val activeOrders: List<Order> = emptyList(),
    val historyOrders: List<Order> = emptyList(),
    val selectedTab: OrdersTab = OrdersTab.ACTIVE,
    val isLoadingActive: Boolean = false,
    val isLoadingHistory: Boolean = false,
    val hasNetworkConnection: Boolean = true,
    val error: String? = null,
) {
    val hasActiveOrders: Boolean get() = activeOrders.isNotEmpty()
    val hasHistoryOrders: Boolean get() = historyOrders.isNotEmpty()

    val isLoading: Boolean
        get() = when (selectedTab) {
            OrdersTab.ACTIVE -> isLoadingActive
            OrdersTab.HISTORY -> isLoadingHistory
        }
}

enum class OrdersTab {
    ACTIVE,
    HISTORY,
}