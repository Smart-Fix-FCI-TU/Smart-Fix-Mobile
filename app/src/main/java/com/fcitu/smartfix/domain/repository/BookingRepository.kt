package com.fcitu.smartfix.domain.repository

import com.fcitu.smartfix.domain.entity.ServiceRequest

interface BookingRepository {
    suspend fun createServiceRequest(request: ServiceRequest): ServiceRequest

    suspend fun sendBookingRequest(
        orderId: String,
        technicianId: String
    )
}