package com.fcitu.smartfix.domain.entity

import kotlinx.datetime.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class Review(
    val id: Uuid,
    val orderId: Uuid,
    val customerId: Uuid,
    val technicianId: Uuid,
    val rating: Int,
    val comment: String,
    val createdAt: LocalDateTime
)