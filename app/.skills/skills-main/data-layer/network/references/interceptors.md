# Interceptors — Complete Implementations

## AuthInterceptor
Adds Bearer token to every outgoing request.

```kotlin
// data/network/interceptor/AuthInterceptor.kt
package com.app.data.network.interceptor

import com.app.data.auth.TokenRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenRepository: TokenRepository,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { tokenRepository.getAccessToken() }
        val request = chain.request().newBuilder().apply {
            if (token != null) {
                header("Authorization", "Bearer $token")
            }
        }.build()
        return chain.proceed(request)
    }
}
```

---

## TokenRefreshInterceptor
Handles 401 responses: refreshes token once, replays original request.
Uses a Mutex to prevent concurrent refresh storms.

```kotlin
// data/network/interceptor/TokenRefreshInterceptor.kt
package com.app.data.network.interceptor

import com.app.data.auth.TokenRepository
import com.app.data.auth.dto.RefreshTokenRequestDto
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.Interceptor
import javax.inject.Inject

class TokenRefreshInterceptor @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val unauthClient: OkHttpClient,   // injected as @UnauthOkHttpClient
    private val baseUrl: String,
) : Interceptor {

    private val mutex = Mutex()

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.proceed(chain.request())

        if (original.code != 401) return original

        return runBlocking {
            mutex.withLock {
                // Another coroutine may have already refreshed while we waited for the lock.
                // Re-check if the token has changed since our original request was made.
                val currentToken = tokenRepository.getAccessToken()
                val requestToken  = chain.request().header("Authorization")
                    ?.removePrefix("Bearer ")

                if (currentToken != null && currentToken != requestToken) {
                    // Token was refreshed by someone else — replay with new token
                    original.close()
                    val retried = chain.request().newBuilder()
                        .header("Authorization", "Bearer $currentToken")
                        .build()
                    return@withLock chain.proceed(retried)
                }

                // Attempt token refresh
                val refreshToken = tokenRepository.getRefreshToken()
                if (refreshToken == null) {
                    // No refresh token → force logout
                    tokenRepository.clearTokens()
                    tokenRepository.notifyAuthExpired()
                    return@withLock original
                }

                val refreshed = tryRefreshToken(refreshToken)
                if (refreshed) {
                    val newToken = tokenRepository.getAccessToken()
                    original.close()
                    val retried = chain.request().newBuilder()
                        .header("Authorization", "Bearer $newToken")
                        .build()
                    chain.proceed(retried)
                } else {
                    // Refresh failed → force logout
                    tokenRepository.clearTokens()
                    tokenRepository.notifyAuthExpired()
                    original
                }
            }
        }
    }

    private suspend fun tryRefreshToken(refreshToken: String): Boolean {
        return try {
            val body = Json.encodeToString(RefreshTokenRequestDto(refreshToken))
                .toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url("${baseUrl}auth/refresh")
                .post(body)
                .build()

            val response = unauthClient.newCall(request).execute()
            if (!response.isSuccessful) return false

            val responseBody = response.body?.string() ?: return false
            val tokens = Json.decodeFromString<TokenResponseDto>(responseBody)
            tokenRepository.saveTokens(
                accessToken  = tokens.accessToken,
                refreshToken = tokens.refreshToken,
            )
            true
        } catch (e: Exception) {
            false
        }
    }
}
```

---

## TokenRepository

```kotlin
// data/auth/TokenRepository.kt
package com.app.data.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    private val _authExpired = MutableSharedFlow<Unit>(replay = 0)
    val authExpiredEvents: Flow<Unit> = _authExpired.asSharedFlow()

    suspend fun getAccessToken(): String? =
        dataStore.data.first()[Keys.ACCESS_TOKEN]

    suspend fun getRefreshToken(): String? =
        dataStore.data.first()[Keys.REFRESH_TOKEN]

    suspend fun saveTokens(accessToken: String, refreshToken: String) {
        dataStore.edit { prefs ->
            prefs[Keys.ACCESS_TOKEN]  = accessToken
            prefs[Keys.REFRESH_TOKEN] = refreshToken
        }
    }

    suspend fun clearTokens() {
        dataStore.edit { it.clear() }
    }

    suspend fun notifyAuthExpired() {
        _authExpired.emit(Unit)
    }

    private object Keys {
        val ACCESS_TOKEN  = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }
}
```

---

## Consuming auth expiry events in MainActivity

```kotlin
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var tokenRepository: TokenRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            tokenRepository.authExpiredEvents.collect {
                // Navigate to login, clear back stack
                navController.navigate(LoginRoute) {
                    popUpTo(navController.graph.id) { inclusive = true }
                }
            }
        }
    }
}
```