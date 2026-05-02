package com.fcitu.smartfix.domain.entity

import com.fcitu.smartfix.domain.model.OrderStatus
import com.fcitu.smartfix.domain.model.ServiceCategory
import kotlinx.datetime.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class ServiceRequest(
    val id: String,
    val customerId: String,
    val technicianId: String,
    val serviceCategory: ServiceCategory,
    val title: String,
    val description: String,
    val problemPhotoUrls: List<String>,
    val beforeRepairPhotoUrls: List<String>,
    val afterRepairPhotoUrls: List<String>,
    val location: Location,
    val address: String,
    val apartment: String,
    val floor: String,
    val additionalNotes: String,
    val status: OrderStatus,
    val requestExpiresAt: LocalDateTime,
    val createdAt: LocalDateTime
)