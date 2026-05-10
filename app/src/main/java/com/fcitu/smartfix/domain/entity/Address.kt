package com.fcitu.smartfix.domain.entity

data class Address(
    val id: String,
    val fullAddress: String,
    val location: Location?,
    val floor: String,
    val apartmentNo: String,
){
    data class Location(
        val latitude: Double,
        val longitude: Double
    )
}