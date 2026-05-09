package com.fcitu.smartfix.data.remote.util

import com.fcitu.smartfix.data.local.UserDataStore
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val userDataStore: UserDataStore
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // Skip adding token for login and refresh endpoints
        if (request.url.encodedPath.contains("api/v1/auth/login") ||
            request.url.encodedPath.contains("api/v1/auth/refresh-token")
        ) {
            return chain.proceed(request)
        }

        val token = userDataStore.cachedAccessToken

        return if (token != null) {
            val authenticatedRequest = request.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
            chain.proceed(authenticatedRequest)
        } else {
            chain.proceed(request)
        }
    }
}