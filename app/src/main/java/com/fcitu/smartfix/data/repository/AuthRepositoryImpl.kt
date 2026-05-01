package com.fcitu.smartfix.data.repository

import com.fcitu.smartfix.data.remote.dto.auth.LoginRequest
import com.fcitu.smartfix.data.remote.service.AuthService
import com.fcitu.smartfix.data.remote.util.safeApiCall
import com.fcitu.smartfix.domain.entity.User
import com.fcitu.smartfix.domain.model.UserRole
import com.fcitu.smartfix.domain.repository.AuthRepository
import com.fcitu.smartfix.domain.repository.IdentityRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class AuthRepositoryImpl(
    private val authService: AuthService,
    private val identityRepository: IdentityRepository
) : AuthRepository {
    @OptIn(ExperimentalUuidApi::class)
    override suspend fun login(
        phoneNumber: String,
        password: String,
        role: UserRole
    ): User {
        val userType = when (role) {
            UserRole.CUSTOMER -> "customer"
            UserRole.TECHNICIAN -> "technician"
        }

        val response = safeApiCall {
            authService.login(
                userType = userType,
                request = LoginRequest(phone = phoneNumber, password = password)
            )
        }

        val loginData = response.data
        identityRepository.saveSession(
            role = role,
            isLoggedIn = true,
            accessToken = loginData.tokens.accessToken,
            refreshToken = loginData.tokens.refreshToken
        )

        // TODO: i need to handle the case when the id is not a valid UUID
        val userDto = loginData.user
        return User(
            id = try { Uuid.parse(userDto.id) } catch (e: Exception) { Uuid.random() },
            phoneNumber = userDto.phone,
            firstName = userDto.fullName.split(" ").firstOrNull() ?: "",
            lastName = userDto.fullName.split(" ").drop(1).joinToString(" "),
            username = userDto.email.split("@").firstOrNull() ?: userDto.fullName,
            email = userDto.email,
            role = role,
            profilePhotoUrl = "",
            location = null
        )
    }
}