package com.fcitu.smartfix.data.repository

import com.fcitu.smartfix.data.remote.mapper.toDomain
import com.fcitu.smartfix.data.remote.service.TechnicianService
import com.fcitu.smartfix.domain.entity.Address
import com.fcitu.smartfix.domain.entity.Technician
import com.fcitu.smartfix.domain.model.ServiceCategory
import com.fcitu.smartfix.domain.repository.TechnicianRepository

class TechnicianRepositoryImpl(
    private val technicianService: TechnicianService
)
    : TechnicianRepository {
    override suspend fun getAvailableTechnicians(
        location: Address.Location,
        serviceCategory: ServiceCategory
    ): List<Technician> {
        TODO("Not yet implemented")
    }

    //TODO: Change the impl for this fun  when api endpoint is finished
    override suspend fun getTechnicianDetails(technicianId: String): Technician{
        val response = technicianService.getTechnicianById(technicianId)

        if (response.success) {
            return response.data?.toDomain()
                ?: throw Exception("Technician data is null")
        } else {
            throw Exception(response.message ?: "Failed to load technician profile")
        }
    }


    override suspend fun updateAvailabilityStatus(isAvailable: Boolean) {
        TODO("Not yet implemented")
    }
}