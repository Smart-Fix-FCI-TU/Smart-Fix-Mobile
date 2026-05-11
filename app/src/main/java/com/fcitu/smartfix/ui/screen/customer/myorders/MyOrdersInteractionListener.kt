package com.fcitu.smartfix.ui.screen.customer.myorders

import com.fcitu.smartfix.domain.entity.Technician

interface MyOrdersInteractionListener {
    fun onTabSelected(tab: OrdersTab)
    fun onCompletedOrderClicked(orderId: String)
    fun onActiveOrderClicked(orderId: String)
    fun onNotificationClicked()
    fun onBackClicked()
    fun onGetTechnicianInfo(technicianId: String): Technician?
}