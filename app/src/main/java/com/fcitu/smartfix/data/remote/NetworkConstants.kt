package com.fcitu.smartfix.data.remote

import com.fcitu.smartfix.BuildConfig

object NetworkConstants {
    const val BASE_URL: String = BuildConfig.BASE_URL

    //  region Authentication endpoints
    const val LOGIN: String = "api/v1/auth/login"
    const val REFRESH_TOKEN = "api/v1/auth/refresh-token"

    // endregion
}