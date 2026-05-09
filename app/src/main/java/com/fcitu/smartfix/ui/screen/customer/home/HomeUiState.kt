package com.fcitu.smartfix.ui.screen.customer.home

import com.fcitu.smartfix.domain.entity.Order
import com.fcitu.smartfix.domain.entity.ServiceItem
import com.fcitu.smartfix.domain.entity.User

data class HomeUiState(
    // Home Screen Data
    val user: User? = null,
    val selectedCategory: String? = null,
    val servicesList: List<ServiceItem> = emptyList(),
    val activeOrders: List<Order> = emptyList(),
    val pendingOrderId: String? = null,

    // Loading States--------------------
    val isLoadingOrders: Boolean = false,
    val isLoadingCustomerInfo: Boolean = false,
    val isLoadingPendingOrder: Boolean = false,
//Error
    val error: String? = null,


    //Network Connection
    val hasNetworkConnection: Boolean = true
) {
    val hasActiveOrders: Boolean
        get() = activeOrders.isNotEmpty()

    val hasPendingOrder: Boolean
        get() = !pendingOrderId.isNullOrEmpty()
}