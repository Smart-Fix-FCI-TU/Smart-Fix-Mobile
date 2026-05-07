package com.fcitu.smartfix.data.remote.service

import com.fcitu.smartfix.data.remote.NetworkConstants.LOGIN
import com.fcitu.smartfix.data.remote.NetworkConstants.REFRESH_TOKEN
import com.fcitu.smartfix.data.remote.dto.auth.LoginRequest
import com.fcitu.smartfix.data.remote.dto.auth.LoginResponse
import com.fcitu.smartfix.data.remote.dto.auth.TokenRefreshRequest
import com.fcitu.smartfix.data.remote.dto.auth.TokenRefreshResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST(LOGIN)
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @POST(REFRESH_TOKEN)
    fun refreshToken(
        @Body request: TokenRefreshRequest
    ): Call<TokenRefreshResponse>
}