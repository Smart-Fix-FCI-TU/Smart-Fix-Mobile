package com.fcitu.smartfix.data.repository

import com.fcitu.smartfix.data.remote.mapper.toDomain
import com.fcitu.smartfix.data.remote.service.TechnicianService
import com.fcitu.smartfix.domain.entity.Address
import com.fcitu.smartfix.domain.entity.Technician
import com.fcitu.smartfix.domain.model.ServiceCategory
import com.fcitu.smartfix.domain.repository.TechnicianRepository

class TechnicianRepositoryImpl(
    private val technicianService: TechnicianService
) : TechnicianRepository {
    override suspend fun getAvailableTechnicians(
        location: Address.Location,
        serviceCategory: ServiceCategory
    ): List<Technician> {
        return emptyList()
    }

    override suspend fun getTechnicianDetails(technicianId: String): Technician {
        val response = technicianService.getTechnicianById(technicianId)

        return if (response.success && response.data != null) {
            response.data.toDomain()
        } else {
            throw Exception(response.message ?: "Failed to load technician profile")
        }
    }


    override suspend fun updateAvailabilityStatus(isAvailable: Boolean) {
        // Do nothing
    }

}