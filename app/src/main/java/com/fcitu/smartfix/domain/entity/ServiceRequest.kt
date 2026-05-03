package com.fcitu.smartfix.domain.entity

import com.fcitu.smartfix.domain.model.OrderStatus
import com.fcitu.smartfix.domain.model.ServiceCategory
import kotlinx.datetime.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
data class Order(
    val id: String,
    val customer: UserInfo,
    val technician: UserInfo,
    val details: OrderDetails,
    val repairPhotos: RepairPhotos,
    val status: OrderStatus,
    val timeline: OrderTimeline,
) {
    data class UserInfo(
        val id: String,
        val name: String
    )

    data class OrderDetails(
        val serviceCategory: ServiceCategory,
        val title: String,
        val description: String,
        val problemPhotoUrls: List<String>,
        val address: Address,
        val additionalNotes: String,
    )

    data class RepairPhotos(
        val beforeRepairUrls: List<String> = emptyList(),
        val afterRepairUrls: List<String> = emptyList()
    )

    data class OrderTimeline(
        val createdAt: LocalDateTime,
        val acceptedAt: LocalDateTime,
        val arrivedAt: LocalDateTime,
        val startedAt: LocalDateTime,
        val completedAt: LocalDateTime,
    )
}