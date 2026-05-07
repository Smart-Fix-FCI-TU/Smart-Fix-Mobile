package com.fcitu.smartfix.data.remote.service

import com.fcitu.smartfix.data.remote.dto.auth.LoginRequest
import com.fcitu.smartfix.data.remote.dto.auth.LoginResponse
import com.fcitu.smartfix.data.remote.dto.auth.TokenRefreshRequest
import com.fcitu.smartfix.data.remote.dto.auth.TokenRefreshResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("api/v1/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @POST("api/v1/auth/refresh-token")
    fun refreshToken(
        @Body request: TokenRefreshRequest
    ): Call<TokenRefreshResponse>
}