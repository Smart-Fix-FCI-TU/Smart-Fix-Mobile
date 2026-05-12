package com.fcitu.smartfix.ui.screen.shared.orderDetails

import com.fcitu.smartfix.domain.entity.Address
import com.fcitu.smartfix.domain.entity.Order
import com.fcitu.smartfix.ui.utils.now
import kotlinx.datetime.LocalDateTime

data class OrderDetailsUiState(
    val orderId: String = "",
    val title: String = "",
    val description: String = "",
    val address: Address = Address(
        id = "",
        fullAddress = "",
        location = null,
        floor = "",
        apartmentNo = ""
    ),
    val problemPhotoUrls: List<String> = emptyList(),
    val timeline: Order.OrderTimeline = Order.OrderTimeline(
        createdAt = LocalDateTime.now(),
        acceptedAt = LocalDateTime.now(),
        onWayAt = LocalDateTime.now(),
        arrivedAt = LocalDateTime.now(),
        startedAt = LocalDateTime.now(),
        completedAt = LocalDateTime.now(),
    ),
    val repairPhotos: Order.RepairPhotos = Order.RepairPhotos(
        beforeRepairUrls = emptyList(),
        afterRepairUrls = emptyList()
    ),
    val showRatingBottomSheet: Boolean = false,
    val isRated: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
)