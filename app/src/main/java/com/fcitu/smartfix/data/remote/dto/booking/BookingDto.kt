package com.fcitu.smartfix.data.remote.dto.booking

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookingDto(

    @SerialName("completedAt")
    val completedAt: String? = null,

    @SerialName("address")
    val address: Address? = null,

    @SerialName("notes")
    val notes: String? = null,

    @SerialName("customerId")
    val customerId: CustomerId? = null,

    @SerialName("startedAt")
    val startedAt: String? = null,

    @SerialName("id")
    val id: String? = null,

    @SerialName("serviceId")
    val serviceId: ServiceId? = null,

    @SerialName("totalPriceCents")
    val totalPriceCents: Int? = null,

    @SerialName("technicianId")
    val technicianId: TechnicianId? = null,

    @SerialName("scheduledAt")
    val scheduledAt: String? = null,

    @SerialName("paymentStatus")
    val paymentStatus: String? = null,

    @SerialName("status")
    val status: String? = null
)

@Serializable
data class Address(

    @SerialName("zip")
    val zip: String? = null,

    @SerialName("country")
    val country: String? = null,

    @SerialName("city")
    val city: String? = null,

    @SerialName("street")
    val street: String? = null,

    @SerialName("state")
    val state: String? = null
)

@Serializable
data class CustomerId(

    @SerialName("phone")
    val phone: String? = null,

    @SerialName("name")
    val name: String? = null,

    @SerialName("id")
    val id: String? = null,

    @SerialName("email")
    val email: String? = null
)

@Serializable
data class TechnicianId(

    @SerialName("specialty")
    val specialty: String? = null,

    @SerialName("phone")
    val phone: String? = null,

    @SerialName("name")
    val name: String? = null,

    @SerialName("id")
    val id: String? = null,

    @SerialName("email")
    val email: String? = null
)

@Serializable
data class ServiceId(

    @SerialName("durationMinutes")
    val durationMinutes: Int? = null,

    @SerialName("name")
    val name: String? = null,

    @SerialName("priceCents")
    val priceCents: Int? = null,

    @SerialName("id")
    val id: String? = null,

    @SerialName("category")
    val category: String? = null
)
