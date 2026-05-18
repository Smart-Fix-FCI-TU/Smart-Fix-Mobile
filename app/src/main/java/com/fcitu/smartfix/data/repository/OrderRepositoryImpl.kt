package com.fcitu.smartfix.data.repository

import com.fcitu.smartfix.data.remote.mapper.toDomain
import com.fcitu.smartfix.data.remote.service.BookingApiService
import com.fcitu.smartfix.data.remote.util.safeApiCall
import com.fcitu.smartfix.domain.entity.Order
import com.fcitu.smartfix.domain.model.OrderStatus
import com.fcitu.smartfix.domain.repository.OrderRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class OrderRepositoryImpl(
    private val bookingApiService: BookingApiService,
) : OrderRepository {

    override suspend fun getCustomerOrders(): List<Order> = coroutineScope {

        val statuses = listOf("accepted", "started", "pending", "completed", "cancelled", "rejected")

        val deferredOrders = statuses.map { status ->
            async { getOrdersByStatus(status) }
        }

        deferredOrders.awaitAll().flatten()
    }

    override suspend fun getOrdersByStatus(status: String): List<Order> {
        return safeApiCall {
            bookingApiService.getBookingByStatus(status).data.map { it.toDomain() }
        }
    }
    override suspend fun getActivePendingOrder(): Order? {
        return safeApiCall {
            bookingApiService.getActiveOrders().data?.toDomain()
        }
    }


    override suspend fun getTechnicianOrders(): List<Order> {
        TODO("Not yet implemented")
    }

    override suspend fun getOrderDetails(orderId: String): Order {
        return safeApiCall {
            val response = bookingApiService.getBookingById(orderId)
            if (response.success && response.data != null) {
                response.data.toDomain()
            } else {
                throw Exception(response.message ?: "Order not found or data is null")
            }
        }}


    override suspend fun acceptOrder(orderId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun declineOrder(orderId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun updateOrderStatus(
        orderId: String,
        status: OrderStatus
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun uploadBeforeRepairPhotos(
        orderId: String,
        photos: List<String>
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun uploadAfterRepairPhotos(
        orderId: String,
        photos: List<String>
    ) {
        TODO("Not yet implemented")
    }
}