package com.fcitu.smartfix.domain.model

enum class OrderStatus {
    WAITING_RESPONSE,
    CONFIRMED,
    ON_WAY,
    ARRIVED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
}

val OrderStatus.isActive: Boolean
    get() = this != OrderStatus.COMPLETED

val OrderStatus.isCompleted: Boolean
    get() = this == OrderStatus.COMPLETED