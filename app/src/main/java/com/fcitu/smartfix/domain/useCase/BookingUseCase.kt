package com.fcitu.smartfix.domain.useCase

import android.net.Uri
import com.fcitu.smartfix.domain.entity.Address
import com.fcitu.smartfix.domain.entity.Order
import com.fcitu.smartfix.domain.model.ServiceCategory
import com.fcitu.smartfix.domain.repository.BookingRepository

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
    ): Result<Unit> {
        return try {
            val orderDetails = Order.OrderDetails(
                serviceCategory = ServiceCategory.PLUMBING, // TODO: map serviceId to ServiceCategory
                title = shortTitle,
                description = detailedDescription,
                problemPhotoUrls = problemPhotos.map { it.toString() },
                address = Address(
                    id = "",
                    fullAddress = location,
                    location = null,
                    floor = floor ?: "",
                    apartmentNo = apartmentNo ?: ""
                ),
                additionalNotes = additionalNotes ?: ""
            )
            bookingRepository.createServiceRequest(orderDetails)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
