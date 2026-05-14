package com.fcitu.smartfix.data.repository

import com.fcitu.smartfix.domain.entity.Address
import com.fcitu.smartfix.domain.entity.Technician
import com.fcitu.smartfix.domain.model.ServiceCategory
import com.fcitu.smartfix.domain.repository.TechnicianRepository

class TechnicianRepositoryImpl : TechnicianRepository {
    override suspend fun getAvailableTechnicians(
        location: Address.Location,
        serviceCategory: ServiceCategory
    ): List<Technician> {
        return emptyList()
    }

    override suspend fun getTechnicianDetails(technicianId: String): Technician {
        throw Exception("Server is down - using mock data in ViewModel")
    }

    override suspend fun updateAvailabilityStatus(isAvailable: Boolean) {
        // Do nothing
    }
}
