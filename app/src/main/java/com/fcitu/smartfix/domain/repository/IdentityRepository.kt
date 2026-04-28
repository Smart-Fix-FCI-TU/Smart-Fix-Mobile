package com.fcitu.smartfix.domain.repository

import com.fcitu.smartfix.domain.model.UserRole
import kotlinx.coroutines.flow.Flow

interface IdentityRepository {
    suspend fun saveSession(role: UserRole, isLoggedIn: Boolean)
    fun getIsLoggedIn(): Flow<Boolean>
    fun getUserRole(): Flow<UserRole?>
    suspend fun clearSession()
}