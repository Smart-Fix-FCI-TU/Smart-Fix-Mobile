package com.fcitu.smartfix.data.remote

import com.fcitu.smartfix.BuildConfig

object NetworkConstants {
    const val BASE_URL: String = BuildConfig.BASE_URL

    //  region Authentication endpoints
    const val LOGIN: String = "api/v1/auth/login"
    const val REFRESH_TOKEN = "api/v1/auth/refresh-token"

    // endregion

    // region Review endpoints
    const val SUBMIT_REVIEW: String = "api/v1/reviews"
    const val BOOKING_REVIEW: String = "api/v1/reviews/booking"
    // endregion

    // region Booking endpoints
    const val BOOKINGS: String = "api/v1/bookings"
    const val ACTIVE_ORDERS: String = "api/v1/bookings/active"
    // endregion

    // region Technician endpoints
    const val TECHNICIANS: String = "api/v1/technicians"
    // endregion
}