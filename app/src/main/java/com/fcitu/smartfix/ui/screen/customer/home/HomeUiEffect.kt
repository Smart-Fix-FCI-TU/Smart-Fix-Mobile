package com.fcitu.smartfix.ui.screen.customer.home

import com.fcitu.smartfix.domain.entity.Order

sealed class HomeUiEffect {
    data class NavigateToBooking(
        val selectedCategory: String,
    ) : HomeUiEffect()

    data class NavigateToOrderDetails(
        val orderId: String,
    ) : HomeUiEffect()

    data object NavigateToSearch : HomeUiEffect()

    data object NavigateToNotifications : HomeUiEffect()

    data class ShowError(
        val message: String,
    ) : HomeUiEffect()

    data class NavigateToAllActiveOrders(val orders: List<Order>) : HomeUiEffect()
}
