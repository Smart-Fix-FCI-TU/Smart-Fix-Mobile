package com.fcitu.smartfix.domain.repository

import com.fcitu.smartfix.domain.entity.Location
import com.fcitu.smartfix.domain.entity.Technician
import com.fcitu.smartfix.domain.model.ServiceCategory
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface TechnicianRepository {
    suspend fun getAvailableTechnicians(
        location: Location,
        serviceCategory: ServiceCategory
    ): List<Technician>

    suspend fun getTechnicianDetails(technicianId: Uuid): Technician

    suspend fun updateAvailabilityStatus(isAvailable: Boolean)
}