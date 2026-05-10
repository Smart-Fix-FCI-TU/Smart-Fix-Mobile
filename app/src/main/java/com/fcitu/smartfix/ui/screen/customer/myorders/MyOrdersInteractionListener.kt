package com.fcitu.smartfix.ui.screen.customer.myorders

interface MyOrdersInteractionListener {
    fun onTabSelected(tab: OrdersTab)
    fun onCompletedOrderClicked(orderId: String)
    fun onActiveOrderClicked(orderId: String)
    fun onNotificationClicked()
    fun onBackClicked()
}