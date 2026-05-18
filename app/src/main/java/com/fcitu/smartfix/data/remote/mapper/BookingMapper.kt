package com.fcitu.smartfix.data.remote.mapper

import com.fcitu.smartfix.data.remote.dto.booking.BookingDto
import com.fcitu.smartfix.domain.entity.Address
import com.fcitu.smartfix.domain.entity.Order
import com.fcitu.smartfix.domain.model.OrderStatus
import com.fcitu.smartfix.domain.model.ServiceCategory
import kotlinx.datetime.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun BookingDto.toDomain(): Order = Order(
    id = id?.takeIf { it.isNotBlank() } ?: Uuid.random().toString(),

    customer = Order.UserInfo(
        id = customerId?.id ?: "",
        name = customerId?.name ?: "",
    ),

    technician = Order.UserInfo(
        id = technicianId?.id ?: "",
        name = technicianId?.name ?: "",
    ),

    details = Order.OrderDetails(
        serviceCategory = serviceId?.category.toServiceCategory(),
        title = serviceId?.name ?: "",
        description = "",
        problemPhotoUrls = emptyList(),
        address = address?.toDomain() ?: Address(
            id = "",
            fullAddress = "",
            location = Address.Location(0.0, 0.0),
            floor = "",
            apartmentNo = ""
        ),        additionalNotes = notes ?: "",
    ),

    repairPhotos = Order.RepairPhotos(),

    status = status.toOrderStatus(),

    timeline = Order.OrderTimeline(
        createdAt = scheduledAt?.toLocalDateTimeOrNull() ?: LocalDateTime(2026, 4, 12, 4, 40),
        acceptedAt = LocalDateTime(2026, 4, 12, 4, 40),
        arrivedAt = LocalDateTime(2026, 4, 12, 4, 40),
        startedAt = startedAt?.toLocalDateTimeOrNull() ?: LocalDateTime(2026, 4, 12, 4, 40),
        onWayAt = startedAt?.toLocalDateTimeOrNull()?:LocalDateTime(2026, 4, 12, 4, 40),
        completedAt = completedAt?.toLocalDateTimeOrNull() ?: LocalDateTime(2026, 4, 12, 4, 40),
    )
)

// ── Address ────────────────────────────────────────────────────────────────
private fun com.fcitu.smartfix.data.remote.dto.booking.Address.toDomain(): Address {
    val fullAddress = listOfNotNull(street, city, state, country)
        .filter { it.isNotBlank() }
        .joinToString(", ")

    return Address(
        id = "",
        fullAddress = fullAddress,
        location = Address.Location(0.0, 0.0),
        floor = "",
        apartmentNo = "",
    )
}

// ── Helpers ────────────────────────────────────────────────────────────────
private fun String?.toOrderStatus(): OrderStatus = when (this) {
    "pending_technician",
    "technician_requested",
    "pending" -> OrderStatus.WAITING_RESPONSE

    "accepted" -> OrderStatus.ASSIGNED
    "started" -> OrderStatus.IN_PROGRESS
    "completed" -> OrderStatus.COMPLETED
    "cancelled" -> OrderStatus.CANCELLED
    "rejected" -> OrderStatus.WAITING_RESPONSE
    else -> OrderStatus.WAITING_RESPONSE
}

private fun String?.toServiceCategory(): ServiceCategory = when (this) {
    "plumbing" -> ServiceCategory.PLUMBING
    "electricity" -> ServiceCategory.ELECTRICITY
    "painting" -> ServiceCategory.PAINTING
    "carpentry" -> ServiceCategory.CARPENTRY
    "conditioning" -> ServiceCategory.CONDITIONING
    else -> ServiceCategory.ELECTRICITY
}

private fun String.toLocalDateTimeOrNull(): LocalDateTime? = try {
    val cleaned = this
        .replace("Z", "")
        .replace(Regex("\\.\\d+$"), "")
    LocalDateTime.parse(cleaned)
} catch (e: Exception) {
    null
}