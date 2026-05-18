package com.fcitu.smartfix.data.remote.mapper


import com.fcitu.smartfix.data.remote.dto.technician.Location
import com.fcitu.smartfix.data.remote.dto.technician.TechnicianDto
import com.fcitu.smartfix.domain.entity.Address
import com.fcitu.smartfix.domain.entity.Technician
import com.fcitu.smartfix.domain.entity.User
import com.fcitu.smartfix.domain.model.ServiceCategory
import com.fcitu.smartfix.domain.model.UserRole

fun TechnicianDto.toDomain(): Technician {
    return Technician(
        user = this.toUser(),
        serviceCategory = mapSpecialtyToCategory(this.specialties?.firstOrNull()),
        isAvailable = this.availability ?: false,
        isOnJob = false,
        yearsOfExperience = 0,
        bio = this.bio ?: "",
        averageRating = this.rating?.toFloat() ?: 0f,
     // TODO: Remove this mapping once the Entity is updated or API provides reviewCount
        reviewCount = this.completedJobs ?: 0,
        reviews = emptyList()
    )
}

private fun TechnicianDto.toUser(): User {
    return User(
        id = this.id ?: "",
        phoneNumber = "",
        firstName = this.firstName ?: "",
        lastName = this.lastName ?: "",
        username = "${this.firstName ?: ""} ${this.lastName ?: ""}".trim(),
        birthOfDate = "",
        nationalId = "",
        email = "",
        role = UserRole.TECHNICIAN,
        profilePhotoUrl = this.profilePictureUrl ?: "",
        address = this.location?.toDomain() ?: Address("", "", Address.Location(0.0, 0.0), "", "")
    )
}

private fun Location.toDomain(): Address {
    val fullAddress = listOfNotNull(city, state, country)
        .filter { it.isNotBlank() }
        .joinToString(", ")

    return Address(
        id = "",
        fullAddress = fullAddress,
        location = Address.Location(0.0, 0.0),
        floor = "",
        apartmentNo = ""
    )
}

private fun mapSpecialtyToCategory(specialty: String?): ServiceCategory {
    return when (specialty?.lowercase()) {
        "plumbing" -> ServiceCategory.PLUMBING
        "electricity", "electrical" -> ServiceCategory.ELECTRICITY
        "painting" -> ServiceCategory.PAINTING
        "carpentry" -> ServiceCategory.CARPENTRY
        "conditioning", "hvac" -> ServiceCategory.CONDITIONING
        else -> ServiceCategory.ELECTRICITY // Default
    }
}