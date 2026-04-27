package com.fcitu.smartfix.domain.entity

import com.fcitu.smartfix.domain.model.ServiceCategory

data class Technician(
    val user: User,
    val serviceCategory: ServiceCategory,
    val isAvailable: Boolean,
    val yearsOfExperience: Int,
    val bio: String,
    val averageRating: Float,
    val reviewCount: Int
)