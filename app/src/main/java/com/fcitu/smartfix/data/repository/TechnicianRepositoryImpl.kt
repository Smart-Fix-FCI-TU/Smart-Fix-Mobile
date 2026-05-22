package com.fcitu.smartfix.data.repository

import com.fcitu.smartfix.data.remote.mapper.toDomain
import com.fcitu.smartfix.data.remote.service.TechnicianService
import com.fcitu.smartfix.domain.entity.Address
import com.fcitu.smartfix.domain.entity.Technician
import com.fcitu.smartfix.domain.entity.User
import com.fcitu.smartfix.domain.model.ServiceCategory
import com.fcitu.smartfix.domain.model.UserRole
import com.fcitu.smartfix.domain.repository.TechnicianRepository
import kotlinx.coroutines.flow.Flow

class TechnicianRepositoryImpl(
    private val technicianService: TechnicianService
) : TechnicianRepository {
    override suspend fun getAvailableTechnicians(
        location: Address.Location,
        serviceCategory: ServiceCategory
    ): List<Technician> {
        return emptyList()
    }

    override suspend fun toggleAvailability(isAvailable: Boolean): Boolean {
        return isAvailable
    }

    override suspend fun getMyProfile(): Technician {
        return Technician(
            user = User(
                id = "technicianId",
                phoneNumber = "563767567262",
                firstName = "Ahmed",
                lastName = "Mostafa",
                username = "Ahmed Mostafa",
                birthOfDate = "2/2/2002",
                nationalId = "25362627246",
                email = "fouad@gmail.com",
                role = UserRole.CUSTOMER,
                profilePhotoUrl = "",
                address = Address(
                    id = "523455",
                    fullAddress = "Tanta",
                    location = Address.Location(30.0, 31.0),
                    floor = "1",
                    apartmentNo = "2"
                )
            ),
            serviceCategory = ServiceCategory.ELECTRICITY,
            isAvailable = true,
            isOnJob = false,
            yearsOfExperience = 5,
            bio = "",
            averageRating = 3.5F,
            reviewCount = 4,
            reviews = emptyList(),
        )
    }

    //TODO: Change the impl for this fun  when api endpoint is finished
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

    override fun observeAvailability(): Flow<Boolean> {
        TODO("Not yet implemented")
    }

}