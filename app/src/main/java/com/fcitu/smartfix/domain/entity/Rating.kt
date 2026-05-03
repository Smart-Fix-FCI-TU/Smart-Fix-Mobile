package com.fcitu.smartfix.domain.entity

import kotlinx.datetime.LocalDateTime

data class Rating(
    val id: String,
    val orderId: String,
    val customerId: String,
    val technicianId: String,
    val rating: Int,
    val comment: String,
    val createdAt: LocalDateTime
)
