package com.fcitu.smartfix.domain.repository

import com.fcitu.smartfix.domain.entity.ServiceRequest
import com.fcitu.smartfix.domain.model.OrderStatus
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface OrderRepository {
    suspend fun getCustomerOrders(): List<ServiceRequest>

    suspend fun getTechnicianOrders(): List<ServiceRequest>

    suspend fun getOrderDetails(orderId: Uuid): ServiceRequest

    suspend fun acceptOrder(orderId: Uuid)

    suspend fun declineOrder(orderId: Uuid)

    suspend fun updateOrderStatus(orderId: Uuid, status: OrderStatus)

    suspend fun uploadBeforeRepairPhotos(orderId: Uuid, photos: List<String>)

    suspend fun uploadAfterRepairPhotos(orderId: Uuid, photos: List<String>)
}