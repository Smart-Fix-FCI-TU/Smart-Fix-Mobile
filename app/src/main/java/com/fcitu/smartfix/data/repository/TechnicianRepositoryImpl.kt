package com.fcitu.smartfix.data.repository

import com.fcitu.smartfix.domain.entity.Address
import com.fcitu.smartfix.domain.entity.Technician
import com.fcitu.smartfix.domain.entity.User
import com.fcitu.smartfix.domain.model.ServiceCategory
import com.fcitu.smartfix.domain.model.UserRole
import com.fcitu.smartfix.domain.repository.TechnicianRepository

class TechnicianRepositoryImpl : TechnicianRepository {
    override suspend fun getAvailableTechnicians(
        location: Address.Location,
        serviceCategory: ServiceCategory
    ): List<Technician> {
        TODO("Not yet implemented")
    }

    //TODO: Change the impl for this fun  when api endpoint is finished
    override suspend fun getTechnicianDetails(technicianId: String): Technician{
        return Technician(
            user = User(
                id = "5425425",
                phoneNumber = "563767567262",
                firstName = "Fouad",
                lastName = "Elmeligy",
                username = "Fouad Elmeligy",
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

    override suspend fun updateAvailabilityStatus(isAvailable: Boolean) {
        TODO("Not yet implemented")
    }
}