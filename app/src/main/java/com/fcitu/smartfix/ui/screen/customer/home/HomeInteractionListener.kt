package com.fcitu.smartfix.ui.screen.customer.home

import com.fcitu.smartfix.domain.entity.Order

interface HomeInteractionListener {
    fun onCategorySelected(category: String)
    fun onChooseServiceClicked()
    fun onOrderClicked(orderId: String)
    fun onNotificationClicked()
    fun onViewAllOrdersClicked(orders: List<Order>)
    fun onNavigateToAvailableTechnicianList(orderId: String)
    fun onTryAgainClicked()
}