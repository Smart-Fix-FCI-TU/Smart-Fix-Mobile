package com.fcitu.smartfix.data.repository

import com.fcitu.smartfix.data.remote.dto.ReviewRequest
import com.fcitu.smartfix.data.remote.service.ReviewService
import com.fcitu.smartfix.data.remote.util.safeApiCall
import com.fcitu.smartfix.data.remote.util.toLocalDateTime
import com.fcitu.smartfix.domain.entity.Review
import com.fcitu.smartfix.domain.repository.ReviewRepository
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
class ReviewRepositoryImpl(
    private val reviewService: ReviewService
) : ReviewRepository {
    override suspend fun submitReview(
        orderId: String,
        rating: Int,
        comment: String
    ) {
        safeApiCall {
            reviewService.submitReview(
                ReviewRequest(
                    bookingId = orderId,
                    rating = rating,
                    comment = comment
                )
            )
        }
    }

    override suspend fun getReviewByBookingId(bookingId: String): Review? {
        return try {
            val response = safeApiCall {
                reviewService.getReviewByBookingId(bookingId)
            }
            Review(
                id = response.id,
                reviewerName = "", // In a real app, this might come from populated field
                rating = response.rating,
                comment = response.comment ?: "",
                createdAt = Instant.parse(response.createdAt).toLocalDateTime()
            )
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getReviewsForTechnician(technicianId: String): List<Review> {
        return emptyList()
    }
}