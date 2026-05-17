package com.fcitu.smartfix.ui.screen.technician.home

import com.fcitu.smartfix.domain.entity.Order
import com.fcitu.smartfix.domain.entity.Technician

data class TechHomeUiState(
    val technician: Technician? = null,
    val isAvailable: Boolean = false,
    val isTogglingAvailability: Boolean = false,
    val isOnJob: Boolean = false,
    val pendingOrders: List<Order> = emptyList(),
    val acceptedOrder: Order? = null,
    val acceptedOrderId: String? = null,
    val isTransitioning: Boolean = false,
    val selectedOrderForSheet: Order? = null,
    val rejectedOrderIds: Set<String> = emptySet(),
    val isLoadingOrders: Boolean = false,
    val isOrderDetailsVisible: Boolean = false,
    val isLoadingActiveOrder: Boolean = false,
    val isLoadingTechnicianInfo: Boolean = false,
    val hasNetworkConnection: Boolean = true,
    val error: String? = null
) {
    val canToggleAvailability: Boolean
        get() = !isOnJob
    val visiblePendingOrders: List<Order>
        get() = pendingOrders.filter { it.id !in rejectedOrderIds }
    val hasOrders: Boolean
        get() = visiblePendingOrders.isNotEmpty()
}