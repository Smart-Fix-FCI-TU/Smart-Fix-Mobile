package com.fcitu.smartfix.data.repository

import com.fcitu.smartfix.domain.entity.Address
import com.fcitu.smartfix.domain.entity.User
import com.fcitu.smartfix.domain.exception.UserNotRegisteredException
import com.fcitu.smartfix.domain.model.UserRole
import com.fcitu.smartfix.domain.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class AuthRepositoryImpl : AuthRepository {
    @OptIn(ExperimentalUuidApi::class)
    override suspend fun login(
        phoneNumber: String,
        password: String,
        role: UserRole
    ): User {
        // Simulate network delay
        delay(1000)

        // Simulate "User Not Registered" error if phone ends with "000"
        if (phoneNumber.endsWith("000")) {
            throw UserNotRegisteredException()
        }

        // Return a mock user
        return User(
            id = Uuid.random().toString(),
            phoneNumber = phoneNumber,
            firstName = "Mock",
            lastName = "User",
            username = "mockuser",
            email = "mock@example.com",
            role = role,
            profilePhotoUrl = "",
            birthOfDate = "2/3/2002",
            nationalId = "444141415345252",
            address = Address(
                id = Uuid.random().toString(),
                fullAddress = "Tanta",
                location = Address.Location(30.0,31.0),
                floor = "1",
                apartmentNo = "2"
            )

        )
    }
}