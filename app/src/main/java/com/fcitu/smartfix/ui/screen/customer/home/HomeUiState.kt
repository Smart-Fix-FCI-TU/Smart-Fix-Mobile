package com.fcitu.smartfix.ui.screen.customer.home

import com.fcitu.smartfix.domain.entity.Order
import com.fcitu.smartfix.domain.entity.ServiceItem

data class HomeUiState(
    // Home Screen Data
    val userState: UserProfileState = UserProfileState.Loading,
    val selectedCategory: String? = null,
    val servicesList: List<ServiceItem> = emptyList(),
    val activeOrders: List<Order> = emptyList(),
    val pendingOrderId: String? = null,

    // Loading States--------------------
    val isLoadingActiveOrders: Boolean = false,
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