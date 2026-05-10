package com.fcitu.smartfix.data.repository

import android.util.Log
import com.fcitu.smartfix.domain.entity.Address
import com.fcitu.smartfix.domain.entity.User
import com.fcitu.smartfix.domain.model.UserRole
import com.fcitu.smartfix.domain.repository.CustomerRepository

class CustomerRepositoryImpl: CustomerRepository {
    override suspend fun getProfile(): User {
        //TODO change Fake Data When use API
        ///Fake Data
        Log.e("Customer Face Data","Fake data")

        return User(
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
            ))
    }
}