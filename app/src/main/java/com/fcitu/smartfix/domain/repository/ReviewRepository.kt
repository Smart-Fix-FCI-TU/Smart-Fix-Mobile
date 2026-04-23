package com.fcitu.smartfix.domain.repository

import com.fcitu.smartfix.domain.entity.Review
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface ReviewRepository {
    suspend fun submitReview(
        orderId: Uuid,
        revieweeId: Uuid,
        rating: Int,
        comment: String
    )

    suspend fun getReviewsForTechnician(technicianId: Uuid): List<Review>
}