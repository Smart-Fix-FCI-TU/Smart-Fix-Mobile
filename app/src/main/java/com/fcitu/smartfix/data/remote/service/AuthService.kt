package com.fcitu.smartfix.data.remote.service

import com.fcitu.smartfix.data.remote.dto.auth.LoginRequest
import com.fcitu.smartfix.data.remote.dto.auth.LoginResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {
    @POST("api/v1/auth/login")
    suspend fun login(
        @Header("user-type") userType: String,
        @Body request: LoginRequest
    ): LoginResponse
}