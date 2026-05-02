package com.fcitu.smartfix.domain.repository

import com.fcitu.smartfix.domain.entity.Location
import com.fcitu.smartfix.domain.entity.Technician
import com.fcitu.smartfix.domain.model.ServiceCategory

interface TechnicianRepository {
    suspend fun getAvailableTechnicians(
        location: Location,
        serviceCategory: ServiceCategory
    ): List<Technician>

    suspend fun getTechnicianDetails(technicianId: String): Technician

    suspend fun updateAvailabilityStatus(isAvailable: Boolean)
}