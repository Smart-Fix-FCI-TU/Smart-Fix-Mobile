package com.fcitu.smartfix.data.remote.util

import com.fcitu.smartfix.data.local.UserDataStore
import com.fcitu.smartfix.data.remote.dto.auth.TokenRefreshRequest
import com.fcitu.smartfix.data.remote.service.AuthService
import com.fcitu.smartfix.domain.model.UserRole
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(
    private val userDataStore: UserDataStore,
    private val authService: AuthService
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        // Only attempt to refresh if the response code is 401
        if (response.code != 401) return null

        // Avoid infinite refresh loops
        if (responseCount(response) >= 3) {
            runBlocking { userDataStore.clearSession() }
            return null
        }

        synchronized(this) {
            val refreshToken = runBlocking { userDataStore.refreshToken.firstOrNull() }
            val role = runBlocking { userDataStore.userRole.firstOrNull() } ?: UserRole.CUSTOMER

            if (refreshToken == null) {
                runBlocking { userDataStore.clearSession() }
                return null
            }

            // Call refresh token endpoint synchronously
            val refreshResponse =
                authService.refreshToken(TokenRefreshRequest(refreshToken)).execute()

            return if (refreshResponse.isSuccessful && refreshResponse.body()?.success == true) {
                val newData = refreshResponse.body()?.data
                if (newData != null) {
                    runBlocking {
                        userDataStore.saveSession(
                            role = role,
                            isLoggedIn = true,
                            accessToken = newData.accessToken,
                            refreshToken = newData.refreshToken
                        )
                    }
                    response.request.newBuilder()
                        .header("Authorization", "Bearer ${newData.accessToken}")
                        .build()
                } else {
                    runBlocking { userDataStore.clearSession() }
                    null
                }
            } else {
                runBlocking { userDataStore.clearSession() }
                null
            }
        }
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var priorResponse = response.priorResponse
        while (priorResponse != null) {
            count++
            priorResponse = priorResponse.priorResponse
        }
        return count
    }
}