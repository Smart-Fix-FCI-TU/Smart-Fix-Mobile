package com.fcitu.smartfix.domain.repository

import com.fcitu.smartfix.domain.entity.Address
import com.fcitu.smartfix.domain.entity.Technician
import com.fcitu.smartfix.domain.model.ServiceCategory
import kotlinx.coroutines.flow.Flow

interface TechnicianRepository {
    suspend fun getAvailableTechnicians(
        location: Address.Location,
        serviceCategory: ServiceCategory
    ): List<Technician>
    suspend fun toggleAvailability(
        isAvailable: Boolean,
    ): Boolean

    suspend fun getMyProfile(): Technician
    suspend fun getTechnicianDetails(technicianId: String): Technician

    suspend fun updateAvailabilityStatus(isAvailable: Boolean)

    fun observeAvailability(): Flow<Boolean>
}