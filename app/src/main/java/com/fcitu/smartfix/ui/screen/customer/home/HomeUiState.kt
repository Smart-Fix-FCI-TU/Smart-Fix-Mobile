package com.fcitu.smartfix.ui.screen.customer.home

import com.fcitu.smartfix.domain.entity.Order
import com.fcitu.smartfix.domain.model.ServiceCategory

data class HomeUiState(
    val customerName: String = "",
    val deliveryAddress: String = "",
    val selectedCategory: String? = null,
    val activeOrders: List<Order> = emptyList(),
    val isLoadingOrders: Boolean = false,
    val isLoadingCustomerInfo: Boolean = false,
    val error: String? = null,
) {
    val hasActiveOrders: Boolean get() = activeOrders.isNotEmpty()
}
