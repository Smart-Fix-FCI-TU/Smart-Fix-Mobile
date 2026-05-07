package com.fcitu.smartfix.domain.entity

import com.fcitu.smartfix.domain.model.UserRole

data class User(
    val id: String,
    val phoneNumber: String,
    val firstName: String,
    val lastName: String,
    val username: String,
    val email: String,
    val role: UserRole,
    val profilePhotoUrl: String,
    val location: Location?
)