package com.fcitu.smartfix.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.fcitu.smartfix.domain.model.UserRole
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserDataStore(
    private val context: Context,
    private val externalScope: CoroutineScope
) {

    @Volatile
    private var _cachedAccessToken: String? = null
    val cachedAccessToken: String? get() = _cachedAccessToken

    companion object {
        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        private val USER_ROLE = stringPreferencesKey("user_role")
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }

    suspend fun saveSession(
        role: UserRole,
        isLoggedIn: Boolean,
        accessToken: String,
        refreshToken: String
    ) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = isLoggedIn
            preferences[USER_ROLE] = role.name
            preferences[ACCESS_TOKEN] = accessToken
            preferences[REFRESH_TOKEN] = refreshToken
        }
    }

    val accessToken: Flow<String?> = context.dataStore.data
        .map { it[ACCESS_TOKEN] }

    val refreshToken: Flow<String?> = context.dataStore.data
        .map { it[REFRESH_TOKEN] }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IS_LOGGED_IN] ?: false
        }

    val userRole: Flow<UserRole?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_ROLE]?.let { UserRole.valueOf(it) }
        }

    init {
        externalScope.launch {
            accessToken.collectLatest { token ->
                _cachedAccessToken = token
            }
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { it.clear() }
    }
}