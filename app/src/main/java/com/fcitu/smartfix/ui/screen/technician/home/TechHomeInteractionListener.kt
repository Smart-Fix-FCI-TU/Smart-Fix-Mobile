package com.fcitu.smartfix.ui.screen.technician.home

import com.fcitu.smartfix.domain.entity.Order

interface TechHomeInteractionListener {
    fun onToggleAvailability(isAvailable: Boolean)
    fun onAcceptOrder(orderId: String)

    fun onRejectOrder(orderId: String)
    fun onViewOrderDetails(order: Order)
    fun onDismissDetailsSheet()
    fun onNotificationClicked()
    fun onContinueActiveJob()
    fun onOrderTimeout(orderId: String)
    fun onTryAgainClicked()

}