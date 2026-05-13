package com.fcitu.smartfix.data.repository

import com.fcitu.smartfix.data.remote.mapper.toDomain
import com.fcitu.smartfix.data.remote.service.BookingApiService
import com.fcitu.smartfix.data.remote.util.safeApiCall
import com.fcitu.smartfix.domain.entity.Order
import com.fcitu.smartfix.domain.model.OrderStatus
import com.fcitu.smartfix.domain.repository.OrderRepository

class OrderRepositoryImpl(
    private val bookingApiService: BookingApiService,
) : OrderRepository {

    override suspend fun getCustomerOrders(): List<Order> {
        return getOrdersByStatus("accepted") +
                getOrdersByStatus("started") +
                getOrdersByStatus("pending") +
                getOrdersByStatus("completed") +
                getOrdersByStatus("cancelled") +
                getOrdersByStatus("rejected")
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
            bookingApiService.getBookingById(orderId).data.toDomain()
        }
    }

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