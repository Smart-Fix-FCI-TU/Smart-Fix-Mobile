package com.fcitu.smartfix.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderDto(
    @SerialName("id")
    val id: String,
    @SerialName("customer")
    val customer: UserInfoDto,
    @SerialName("technician")
    val technician: UserInfoDto?,
    @SerialName("details")
    val details: OrderDetailsDto,
    @SerialName("repairPhotos")
    val repairPhotos: RepairPhotosDto,
    @SerialName("status")
    val status: String,
    @SerialName("timeline")
    val timeline: OrderTimelineDto,
) {

    @Serializable
    data class UserInfoDto(
        @SerialName("id")
        val id: String,
        @SerialName("name")
        val name: String,
    )

    @Serializable
    data class OrderDetailsDto(
        @SerialName("serviceCategory")
        val serviceCategory: String,
        @SerialName("title")
        val title: String,
        @SerialName("description")
        val description: String,
        @SerialName("problemPhotoUrls")
        val problemPhotoUrls: List<String>,
        @SerialName("address")
        val address: AddressDto,
        @SerialName("additional_notes")
        val additionalNotes: String,
    )

    @Serializable
    data class RepairPhotosDto(
        @SerialName("beforeRepairUrls")
        val beforeRepairUrls: List<String> = emptyList(),
        @SerialName("afterRepairUrls")
        val afterRepairUrls: List<String> = emptyList(),
    )

    @Serializable
    data class OrderTimelineDto(
        @SerialName("createdAt")
        val createdAt: String,
        @SerialName("acceptedAt")
        val acceptedAt: String,
        @SerialName("arrivedAt")
        val arrivedAt: String,
        @SerialName("startedAt")
        val startedAt: String,
        @SerialName("completedAt")
        val completedAt: String,
    )
    @Serializable
    data class AddressDto(
        @SerialName("id")
        val id: String,
        @SerialName("fullName")
        val fullAddress: String,
        @SerialName("location")
        val location: LocationDto?,
        @SerialName("floor")
        val floor: String,
        @SerialName("apartmentNo")
        val apartmentNo: String,
    ){
        @Serializable
        data class LocationDto(
            @SerialName("latitude")
            val latitude: Double,
            @SerialName("longitude")
            val longitude: Double
        )
    }
}