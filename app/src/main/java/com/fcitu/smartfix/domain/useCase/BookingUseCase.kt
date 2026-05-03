package com.fcitu.smartfix.domain.useCase

import android.net.Uri
import com.fcitu.smartfix.domain.entity.Location
import com.fcitu.smartfix.domain.entity.ServiceRequest
import com.fcitu.smartfix.domain.model.OrderStatus
import com.fcitu.smartfix.domain.model.ServiceCategory
import com.fcitu.smartfix.domain.repository.BookingRepository
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class, kotlin.time.ExperimentalTime::class)
class BookingUseCase(
    private val bookingRepository: BookingRepository,
) {
    suspend operator fun invoke(
        serviceId: String,
        shortTitle: String,
        detailedDescription: String,
        problemPhotos: List<Uri>,
        location: String,
        floor: String?,
        apartmentNo: String?,
        additionalNotes: String?
    ): Result<ServiceRequest> {
        return try {
            val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

            // Note: In a real app, photos would be uploaded and URIs converted to URLs.
            // Using placeholder logic for now.
            val request = ServiceRequest(
                id = Uuid.random(),
                customerId = Uuid.random(), // Should come from a SessionManager
                technicianId = Uuid.NIL, // To be assigned
                serviceCategory = ServiceCategory.PLUMBING, // Should be determined by serviceId
                title = shortTitle,
                description = detailedDescription,
                problemPhotoUrls = problemPhotos.map { it.toString() },
                beforeRepairPhotoUrls = emptyList(),
                afterRepairPhotoUrls = emptyList(),
                location = Location(0.0, 0.0), // Should be parsed from location string or passed as Location
                address = location,
                apartment = apartmentNo ?: "",
                floor = floor ?: "",
                additionalNotes = additionalNotes ?: "",
                status = OrderStatus.WAITING_RESPONSE,
                requestExpiresAt = now, // Should be set in the future
                createdAt = now
            )
            val result = bookingRepository.createServiceRequest(request)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}