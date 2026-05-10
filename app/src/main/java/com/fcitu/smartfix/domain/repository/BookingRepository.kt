package com.fcitu.smartfix.domain.repository

import com.fcitu.smartfix.domain.entity.Order

interface BookingRepository {
    suspend fun createServiceRequest(request: Order.OrderDetails): Unit

    suspend fun sendBookingRequest(
        orderId: String,
        technicianId: String
    )
}