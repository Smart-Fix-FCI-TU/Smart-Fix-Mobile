package com.fcitu.smartfix.domain.useCase

import com.fcitu.smartfix.domain.entity.Technician
import com.fcitu.smartfix.domain.repository.TechnicianRepository

class GetTechnicianProfileUseCase(
    private val technicianRepository: TechnicianRepository
) {
    suspend operator fun invoke(technicianId: String): Technician {
        return technicianRepository.getTechnicianDetails(technicianId)
    }
}
