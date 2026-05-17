package com.fcitu.smartfix.domain.repository

import com.fcitu.smartfix.domain.entity.Review

interface ReviewRepository {
    suspend fun submitReview(
        orderId: String,
        rating: Int,
        comment: String
    )

    suspend fun getReviewByBookingId(bookingId: String): Review?

    suspend fun getReviewsForTechnician(technicianId: String): List<Review>
}