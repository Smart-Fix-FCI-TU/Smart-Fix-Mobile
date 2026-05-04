package com.fcitu.smartfix.ui.screen.customer.home

import com.fcitu.smartfix.domain.entity.Order
import com.fcitu.smartfix.domain.model.ServiceCategory

interface HomeInteractionListener {
    fun onCategorySelected(category: String)
    fun onChooseServiceClicked()
    fun onOrderClicked(orderId: String)
    fun onSearchClicked()
    fun onNotificationClicked()

    fun onViewAllOrdersClicked(order: List<Order>)
}