package com.fcitu.smartfix.domain.repository

import com.fcitu.smartfix.domain.entity.ServiceRequest
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface OrderRepository {
    suspend fun getCustomerOrders(): List<ServiceRequest>

    suspend fun getTechnicianOrders(): List<ServiceRequest>

    suspend fun getOrderDetails(orderId: Uuid): ServiceRequest

    suspend fun acceptOrder(orderId: Uuid)

    suspend fun declineOrder(orderId: Uuid)

    suspend fun markOrderCompleted(orderId: Uuid)
}