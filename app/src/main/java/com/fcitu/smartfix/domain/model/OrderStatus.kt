package com.fcitu.smartfix.domain.model

enum class OrderStatus {
    WAITING_RESPONSE,
    ASSIGNED,
    ON_WAY,
    ARRIVED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
}

val OrderStatus.isActive: Boolean
    get() = this != OrderStatus.COMPLETED && this != OrderStatus.WAITING_RESPONSE

val OrderStatus.isCompleted: Boolean
    get() = this == OrderStatus.COMPLETED


val OrderStatus.isPendingRequest: Boolean
    get() = this == OrderStatus.WAITING_RESPONSE