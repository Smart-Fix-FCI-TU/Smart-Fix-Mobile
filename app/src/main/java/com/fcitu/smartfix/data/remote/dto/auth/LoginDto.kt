package com.fcitu.smartfix.data.remote.dto.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    @SerialName("phone") val phone: String,
    @SerialName("password") val password: String
)

@Serializable
data class LoginResponse(
    @SerialName("message") val message: String,
    @SerialName("statusCode") val statusCode: Int,
    @SerialName("data") val data: LoginData
)

@Serializable
data class LoginData(
    @SerialName("message") val message: String,
    @SerialName("user") val user: UserDto,
    @SerialName("tokens") val tokens: TokensDto
)

@Serializable
data class UserDto(
    @SerialName("id") val id: String,
    @SerialName("email") val email: String,
    @SerialName("fullName") val fullName: String,
    @SerialName("phone") val phone: String,
    @SerialName("role") val role: String
)

@Serializable
data class TokensDto(
    @SerialName("accessToken") val accessToken: String,
    @SerialName("refreshToken") val refreshToken: String
)

@Serializable
data class ErrorResponse(
    @SerialName("message") val message: String,
    @SerialName("error") val error: String? = null,
    @SerialName("statusCode") val statusCode: Int
)