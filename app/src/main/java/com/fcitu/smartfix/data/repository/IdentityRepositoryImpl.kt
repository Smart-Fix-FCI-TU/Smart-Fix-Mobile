package com.fcitu.smartfix.data.repository

import com.fcitu.smartfix.data.local.UserDataStore
import com.fcitu.smartfix.domain.model.UserRole
import com.fcitu.smartfix.domain.repository.IdentityRepository
import kotlinx.coroutines.flow.Flow

class IdentityRepositoryImpl(
    private val userDataStore: UserDataStore
) : IdentityRepository {

    override suspend fun saveSession(role: UserRole, isLoggedIn: Boolean, accessToken: String, refreshToken: String) {
        userDataStore.saveSession(role, isLoggedIn, accessToken, refreshToken)
    }

    override fun getIsLoggedIn(): Flow<Boolean> {
        return userDataStore.isLoggedIn
    }

    override fun getUserRole(): Flow<UserRole?> {
        return userDataStore.userRole
    }

    override suspend fun clearSession() {
        userDataStore.clearSession()
    }
}