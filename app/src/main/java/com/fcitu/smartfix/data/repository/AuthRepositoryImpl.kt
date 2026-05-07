package com.fcitu.smartfix.data.repository

import com.fcitu.smartfix.data.remote.dto.auth.LoginRequest
import com.fcitu.smartfix.data.remote.service.AuthService
import com.fcitu.smartfix.data.remote.util.safeApiCall
import com.fcitu.smartfix.domain.entity.User
import com.fcitu.smartfix.domain.model.UserRole
import com.fcitu.smartfix.domain.repository.AuthRepository
import com.fcitu.smartfix.domain.repository.IdentityRepository
import kotlin.uuid.ExperimentalUuidApi

class AuthRepositoryImpl(
    private val authService: AuthService,
    private val identityRepository: IdentityRepository
) : AuthRepository {
    @OptIn(ExperimentalUuidApi::class)
    override suspend fun login(
        email: String,
        password: String
    ): User {
        val response = safeApiCall {
            authService.login(
                request = LoginRequest(email = email, password = password)
            )
        }

        val loginData = response.data
        val userDto = loginData.user

        val role = when (userDto.role.lowercase()) {
            "customer" -> UserRole.CUSTOMER
            "technician" -> UserRole.TECHNICIAN
            else -> UserRole.CUSTOMER // Default or throw error
        }

        identityRepository.saveSession(
            role = role,
            isLoggedIn = true,
            accessToken = loginData.accessToken,
            refreshToken = loginData.refreshToken
        )

        return User(
            id = userDto.id,
            phoneNumber = userDto.phone,
            firstName = userDto.name.split(" ").firstOrNull() ?: "",
            lastName = userDto.name.split(" ").drop(1).joinToString(" "),
            username = userDto.email.split("@").firstOrNull() ?: userDto.name,
            email = userDto.email,
            role = role,
            profilePhotoUrl = userDto.avatarUrl ?: "",
            location = null
        )
    }
}