package com.fcitu.smartfix.domain.repository

import com.fcitu.smartfix.domain.entity.ServiceRequest
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface BookingRepository {
    suspend fun createServiceRequest(request: ServiceRequest): ServiceRequest

    suspend fun sendBookingRequest(
        orderId: Uuid,
        technicianId: Uuid
    )
}