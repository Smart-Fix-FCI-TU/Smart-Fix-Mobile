package com.fcitu.smartfix.data.repository

import com.fcitu.smartfix.domain.entity.Order
import com.fcitu.smartfix.domain.repository.BookingRepository
import kotlinx.coroutines.delay

class BookingRepositoryImpl : BookingRepository {
    override suspend fun createServiceRequest(request: Order.OrderDetails) {
        // Simulate network call delay
        delay(1500)
    }

    override suspend fun sendBookingRequest(orderId: String, technicianId: String) {
        delay(1000)
    }
}
