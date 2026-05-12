package com.fcitu.smartfix.data.repository

import com.fcitu.smartfix.domain.entity.Address
import com.fcitu.smartfix.domain.entity.Order
import com.fcitu.smartfix.domain.model.OrderStatus
import com.fcitu.smartfix.domain.model.ServiceCategory
import com.fcitu.smartfix.domain.repository.OrderRepository
import kotlinx.datetime.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class OrderRepositoryImpl : OrderRepository {

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun getCustomerOrders(): List<Order> {
        // Fake Data
        return List(4) {
                Order(
                    id = Uuid.random().toString(),
                    customer = Order.UserInfo(id = Uuid.random().toString(), name = "Fouad"),
                    technician = Order.UserInfo(id = Uuid.random().toString(), name = "Ahmed Mohamed"),
                    details = Order.OrderDetails(
                        serviceCategory = ServiceCategory.ELECTRICITY,
                        title = "change lamb",
                        "the lamb is broken", problemPhotoUrls = emptyList(),
                        address = Address(
                            id = Uuid.random().toString(),
                            fullAddress = "Tanta",
                            location = Address.Location(40.0, 41.0),
                            floor = "2",
                            apartmentNo = "3"
                        ), additionalNotes = ""
                    ),
                    repairPhotos = Order.RepairPhotos(
                        beforeRepairUrls = emptyList(),
                        afterRepairUrls = emptyList()
                    ),
                    status = OrderStatus.ON_WAY, timeline = Order.OrderTimeline(
                        createdAt   = LocalDateTime(2024, 1, 15, 10, 30),
                        acceptedAt  = LocalDateTime(2024, 1, 15, 11, 0),
                        onWayAt     = LocalDateTime(2024, 1, 15, 11, 30),
                        arrivedAt   = LocalDateTime(2024, 1, 15, 12, 0),
                        startedAt   = LocalDateTime(2024, 1, 15, 12, 30),
                        completedAt = LocalDateTime(2024, 1, 15, 12, 30),
                    )
                )
        }
    }

    override suspend fun getTechnicianOrders(): List<Order> {
        TODO("Not yet implemented")
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun getOrderDetails(orderId: String): Order {
        return Order(
            id = orderId,
            customer = Order.UserInfo(
                id = Uuid.random().toString(),
                name = "Fouad"
            ),
            technician = Order.UserInfo(
                id = Uuid.random().toString(),
                name = "Ahmed Mohamed"
            ),
            details = Order.OrderDetails(
                serviceCategory = ServiceCategory.ELECTRICITY,
                title = "change lamb",
                description = "the lamb is broken",
                problemPhotoUrls = listOf(
                    "https://cdn.stocksnap.io/img-thumbs/960w/sand-dunes_Q8FBYHQ2ER.jpg",
                    "https://cdn.stocksnap.io/img-thumbs/960w/sand-dunes_Q8FBYHQ2ER.jpg",
                    "https://cdn.stocksnap.io/img-thumbs/960w/sand-dunes_Q8FBYHQ2ER.jpg",
                ),
                address = Address(
                    id = Uuid.random().toString(),
                    fullAddress = "Tanta, Egypt",
                    location = null,
                    floor = "2",
                    apartmentNo = "3"
                ),
                additionalNotes = "Please be careful when changing the lamb, it's a bit tricky to access it."
            ),
            repairPhotos = Order.RepairPhotos(
                beforeRepairUrls = listOf(
                    "https://cdn.stocksnap.io/img-thumbs/960w/sand-dunes_Q8FBYHQ2ER.jpg",
                    "https://cdn.stocksnap.io/img-thumbs/960w/sand-dunes_Q8FBYHQ2ER.jpg",
                ),
                afterRepairUrls = listOf(
                    "https://cdn.stocksnap.io/img-thumbs/960w/sand-dunes_Q8FBYHQ2ER.jpg",
                    "https://cdn.stocksnap.io/img-thumbs/960w/sand-dunes_Q8FBYHQ2ER.jpg",
                )
            ),
            status = OrderStatus.COMPLETED,
            timeline = Order.OrderTimeline(
                createdAt = LocalDateTime(2024, 1, 15, 10, 30),
                acceptedAt = LocalDateTime(2024, 1, 15, 11, 0),
                onWayAt = LocalDateTime(2024, 1, 15, 11, 30),
                arrivedAt = LocalDateTime(2024, 1, 15, 12, 0),
                startedAt = LocalDateTime(2024, 1, 15, 12, 30),
                completedAt = LocalDateTime(2024, 1, 15, 12, 30),
            )
        )
    }

    override suspend fun acceptOrder(orderId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun declineOrder(orderId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun updateOrderStatus(
        orderId: String,
        status: OrderStatus
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun uploadBeforeRepairPhotos(
        orderId: String,
        photos: List<String>
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun uploadAfterRepairPhotos(
        orderId: String,
        photos: List<String>
    ) {
        TODO("Not yet implemented")
    }
}