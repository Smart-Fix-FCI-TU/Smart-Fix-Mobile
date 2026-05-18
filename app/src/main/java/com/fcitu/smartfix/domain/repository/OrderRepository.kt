package com.fcitu.smartfix.domain.repository

import com.fcitu.smartfix.domain.entity.Order
import com.fcitu.smartfix.domain.model.OrderStatus

interface OrderRepository {
    suspend fun getCustomerOrders(): List<Order>
    suspend fun getOrdersByStatus(status: String): List<Order>
    suspend fun getActivePendingOrder(): Order?
    suspend fun getTechnicianOrders(): List<Order>

    suspend fun getOrderDetails(orderId: String): Order

    suspend fun acceptOrder(orderId: String)

    suspend fun declineOrder(orderId: String)

    suspend fun updateOrderStatus(orderId: String, status: OrderStatus)

    suspend fun uploadBeforeRepairPhotos(orderId: String, photos: List<String>)

    suspend fun uploadAfterRepairPhotos(orderId: String, photos: List<String>)
}