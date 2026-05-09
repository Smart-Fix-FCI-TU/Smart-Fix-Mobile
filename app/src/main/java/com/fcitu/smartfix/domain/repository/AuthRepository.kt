package com.fcitu.smartfix.domain.repository

import com.fcitu.smartfix.domain.entity.User
import com.fcitu.smartfix.domain.model.UserRole

interface AuthRepository {
    suspend fun login(
        email: String,
        password: String
    ): User
}