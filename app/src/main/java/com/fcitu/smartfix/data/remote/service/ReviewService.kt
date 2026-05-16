package com.fcitu.smartfix.data.remote.service

import com.fcitu.smartfix.data.remote.NetworkConstants.BOOKING_REVIEW
import com.fcitu.smartfix.data.remote.NetworkConstants.SUBMIT_REVIEW
import com.fcitu.smartfix.data.remote.dto.ReviewDto
import com.fcitu.smartfix.data.remote.dto.ReviewRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ReviewService {
    @POST(SUBMIT_REVIEW)
    suspend fun submitReview(@Body request: ReviewRequest): ReviewDto

    @GET("$BOOKING_REVIEW/{bookingId}")
    suspend fun getReviewByBookingId(@Path("bookingId") bookingId: String): ReviewDto
}