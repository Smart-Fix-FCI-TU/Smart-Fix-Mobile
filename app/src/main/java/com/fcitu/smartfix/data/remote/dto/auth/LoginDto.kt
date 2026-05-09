package com.fcitu.smartfix.data.remote.dto.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    @SerialName("email") val email: String,
    @SerialName("password") val password: String
)

@Serializable
data class LoginResponse(
    @SerialName("success") val success: Boolean,
    @SerialName("data") val data: LoginData,
    @SerialName("error") val error: String? = null,
    @SerialName("code") val code: String? = null
)

@Serializable
data class LoginData(
    @SerialName("accessToken") val accessToken: String,
    @SerialName("refreshToken") val refreshToken: String,
    @SerialName("user") val user: UserDto,
    @SerialName("isVerified") val isVerified: Boolean
)

@Serializable
data class UserDto(
    @SerialName("_id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("email") val email: String,
    @SerialName("phone") val phone: String,
    @SerialName("role") val role: String,
    @SerialName("avatarUrl") val avatarUrl: String? = null,
    @SerialName("language") val language: String? = "en",
    @SerialName("theme") val theme: String? = "light",
    @SerialName("isActive") val isActive: Boolean? = true,
    @SerialName("isVerified") val isVerified: Boolean? = false,
    @SerialName("privacySettings") val privacySettings: PrivacySettingsDto? = null,
    @SerialName("notificationSettings") val notificationSettings: NotificationSettingsDto? = null
)

@Serializable
data class PrivacySettingsDto(
    @SerialName("showPhone") val showPhone: Boolean,
    @SerialName("showEmail") val showEmail: Boolean,
    @SerialName("showOnlineStatus") val showOnlineStatus: Boolean
)

@Serializable
data class NotificationSettingsDto(
    @SerialName("bookingUpdates") val bookingUpdates: Boolean,
    @SerialName("chatMessages") val chatMessages: Boolean,
    @SerialName("promotions") val promotions: Boolean
)

@Serializable
data class TokenRefreshRequest(
    @SerialName("refreshToken") val refreshToken: String
)

@Serializable
data class TokenRefreshResponse(
    @SerialName("success") val success: Boolean,
    @SerialName("data") val data: TokenRefreshData
)

@Serializable
data class TokenRefreshData(
    @SerialName("accessToken") val accessToken: String,
    @SerialName("refreshToken") val refreshToken: String
)

@Serializable
data class ErrorResponse(
    @SerialName("success") val success: Boolean,
    @SerialName("error") val error: String,
    @SerialName("code") val code: String
)