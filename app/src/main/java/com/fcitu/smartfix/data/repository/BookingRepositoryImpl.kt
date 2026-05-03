package com.fcitu.smartfix.data.repository

import com.fcitu.smartfix.domain.entity.ServiceRequest
import com.fcitu.smartfix.domain.repository.BookingRepository
import kotlinx.coroutines.delay
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class BookingRepositoryImpl : BookingRepository {
    override suspend fun createServiceRequest(request: ServiceRequest): ServiceRequest {
        // Simulate network call delay
        delay(1500)
        // In a real application, this would involve making an actual network request
        return request
    }

    override suspend fun sendBookingRequest(orderId: Uuid, technicianId: Uuid) {
        delay(1000)
    }
}