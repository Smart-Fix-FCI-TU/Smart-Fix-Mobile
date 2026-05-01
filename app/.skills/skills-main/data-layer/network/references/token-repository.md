# TokenRepository — Shared Auth State

Used by both Retrofit interceptors and Ktor auth plugin. Single source of truth
for access/refresh tokens. Backed by DataStore (not SharedPreferences).

---

## Dependencies

```kotlin
dependencies {
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    // For encryption (recommended for production):
    implementation("androidx.security:security-crypto:1.1.0-alpha06")
}
```

---

## Implementation

```kotlin
// data/auth/TokenRepository.kt
package com.fcitu.smartfix.data.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class TokenRepository(
    private val dataStore: DataStore<Preferences>,
) {
    // ─── Session-expired event bus ─────────────────────────────────────────────
    // MainActivity / NavHost collects this to force-navigate to login
    private val _sessionExpired = MutableSharedFlow<Unit>(replay = 0)
    val sessionExpiredEvents: Flow<Unit> = _sessionExpired.asSharedFlow()

    // ─── Token accessors ───────────────────────────────────────────────────────

    suspend fun getAccessToken(): String? =
        dataStore.data.first()[Keys.ACCESS_TOKEN]

    suspend fun getRefreshToken(): String? =
        dataStore.data.first()[Keys.REFRESH_TOKEN]

    fun observeAccessToken(): Flow<String?> =
        dataStore.data.map { it[Keys.ACCESS_TOKEN] }

    val isLoggedIn: Flow<Boolean> =
        dataStore.data.map { it[Keys.ACCESS_TOKEN] != null }

    // ─── Mutations ─────────────────────────────────────────────────────────────

    suspend fun saveTokens(accessToken: String, refreshToken: String) {
        dataStore.edit { prefs ->
            prefs[Keys.ACCESS_TOKEN]  = accessToken
            prefs[Keys.REFRESH_TOKEN] = refreshToken
        }
    }

    suspend fun clearTokens() {
        dataStore.edit { it.clear() }
    }

    suspend fun notifySessionExpired() {
        _sessionExpired.emit(Unit)
    }

    // ─── Keys ──────────────────────────────────────────────────────────────────

    private object Keys {
        val ACCESS_TOKEN  = stringPreferencesKey("smartfix_access_token")
        val REFRESH_TOKEN = stringPreferencesKey("smartfix_refresh_token")
    }
}
```

---

## Providing DataStore in Koin

```kotlin
// di/appModule.kt
package com.fcitu.smartfix.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private val Context.dataStore by preferencesDataStore(name = "smartfix_prefs")

val appModule = module {
    single { androidContext().dataStore }
    single { TokenRepository(dataStore = get()) }
}
```

---

## Consuming session-expired events in your NavHost / MainActivity

```kotlin
// In your root composable or MainActivity
@Composable
fun SmartFixNavHost(
    tokenRepository: TokenRepository = koinInject(),
    navController: NavHostController,
) {
    // Force logout whenever the token refresh fails
    LaunchedEffect(Unit) {
        tokenRepository.sessionExpiredEvents.collect {
            navController.navigate(AuthRoute) {
                popUpTo(navController.graph.id) { inclusive = true }
            }
        }
    }

    NavHost(navController = navController, startDestination = /* ... */) {
        // your nav graph
    }
}
```

---

## Login / Logout flow in ViewModel

```kotlin
// After successful login API call:
tokenRepository.saveTokens(
    accessToken  = response.accessToken,
    refreshToken = response.refreshToken,
)

// On explicit logout:
suspend fun logout() {
    tokenRepository.clearTokens()
    _navEvents.emit(AuthNavEvent.GoToLogin)
}
```

---

## Security note

For production, encrypt the DataStore. Use `EncryptedSharedPreferences` as the backing
storage or wrap `DataStore` with the `security-crypto` library:

```kotlin
// Encrypted DataStore setup (replaces the plain preferencesDataStore above)
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

private fun Context.createEncryptedDataStore(): DataStore<Preferences> {
    val masterKey = MasterKey.Builder(this)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    return PreferenceDataStoreFactory.create {
        // Store in encrypted shared preferences file
        EncryptedSharedPreferences.create(
            this,
            "smartfix_encrypted_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
        // Note: use a proper EncryptedFile-backed DataStore in production
        // The above is illustrative — see the Jetpack Security docs for DataStore
        File(this.filesDir, "smartfix_prefs.preferences_pb")
    }
}
```