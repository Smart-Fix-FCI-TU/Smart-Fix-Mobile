package com.fcitu.smartfix.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewRequest(
    @SerialName("bookingId")
    val bookingId: String,
    @SerialName("rating")
    val rating: Int,
    @SerialName("comment")
    val comment: String? = null
)

@Serializable
data class ReviewDto(
    @SerialName("id")
    val id: String,
    @SerialName("bookingId")
    val bookingId: String,
    @SerialName("customerId")
    val customerId: String,
    @SerialName("technicianId")
    val technicianId: String,
    @SerialName("rating")
    val rating: Int,
    @SerialName("comment")
    val comment: String? = null,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("updatedAt")
    val updatedAt: String? = null
)