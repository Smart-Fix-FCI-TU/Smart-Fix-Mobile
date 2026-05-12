package com.fcitu.smartfix.ui.screen.customer.myorders

sealed interface MyOrdersUiEffect {
    data class NavigateToCompletedOrderDetails(val orderId: String) : MyOrdersUiEffect
    data class NavigateToTrackingActiveOrder(val orderId: String) : MyOrdersUiEffect
    data object NavigateToNotifications : MyOrdersUiEffect
    data object NavigateBack : MyOrdersUiEffect

    data class NavigateToChat(val orderId: String,val technicianId: String) : MyOrdersUiEffect
    data class ShowError(val message: String) : MyOrdersUiEffect
}
