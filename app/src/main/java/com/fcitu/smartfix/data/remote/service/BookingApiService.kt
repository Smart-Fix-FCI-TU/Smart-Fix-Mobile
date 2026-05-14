package com.fcitu.smartfix.data.remote.service

import com.fcitu.smartfix.data.remote.util.ApiResponse
import com.fcitu.smartfix.data.remote.util.PaginatedResponse
import com.fcitu.smartfix.data.remote.dto.booking.BookingDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BookingApiService {

    @GET("/api/v1/bookings")
    suspend fun getBookingByStatus(
        @Query("status") status: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): PaginatedResponse<BookingDto>

    @GET("/api/v1/bookings/active")
    suspend fun getActiveOrders(): ApiResponse<BookingDto>

    @GET("/api/v1/bookings/{bookingId}")
    suspend fun getBookingById(
        @Path("bookingId") bookingId: String,
    ): ApiResponse<BookingDto>
}