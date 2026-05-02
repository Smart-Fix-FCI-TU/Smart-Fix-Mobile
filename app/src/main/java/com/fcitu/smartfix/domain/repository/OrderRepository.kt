package com.fcitu.smartfix.domain.repository

import com.fcitu.smartfix.domain.entity.ServiceRequest
import com.fcitu.smartfix.domain.model.OrderStatus

interface OrderRepository {
    suspend fun getCustomerOrders(): List<ServiceRequest>

    suspend fun getTechnicianOrders(): List<ServiceRequest>

    suspend fun getOrderDetails(orderId: String): ServiceRequest

    suspend fun acceptOrder(orderId: String)

    suspend fun declineOrder(orderId: String)

    suspend fun updateOrderStatus(orderId: String, status: OrderStatus)

    suspend fun uploadBeforeRepairPhotos(orderId: String, photos: List<String>)

    suspend fun uploadAfterRepairPhotos(orderId: String, photos: List<String>)
}