package com.fcitu.smartfix.data.repository

import com.fcitu.smartfix.data.remote.mapper.toDomain
import com.fcitu.smartfix.data.remote.service.BookingApiService
import com.fcitu.smartfix.data.remote.util.safeApiCall
import com.fcitu.smartfix.domain.entity.Address
import com.fcitu.smartfix.domain.entity.Order
import com.fcitu.smartfix.domain.model.OrderStatus
import com.fcitu.smartfix.domain.model.ServiceCategory
import com.fcitu.smartfix.domain.repository.OrderRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class OrderRepositoryImpl(
    private val bookingApiService: BookingApiService,
) : OrderRepository {

    override suspend fun getCustomerOrders(): List<Order> = coroutineScope {

        val statuses =
            listOf("accepted", "started", "pending", "completed", "cancelled", "rejected")

        val deferredOrders = statuses.map { status ->
            async { getOrdersByStatus(status) }
        }

        deferredOrders.awaitAll().flatten()
    }

    override suspend fun getOrdersByStatus(status: String): List<Order> {
        return safeApiCall {
            bookingApiService.getBookingByStatus(status).data.map { it.toDomain() }
        }
    }

    override suspend fun getActivePendingOrder(): Order? {
        return safeApiCall {
            bookingApiService.getActiveOrders().data?.toDomain()
        }
    }


    override suspend fun getTechnicianOrders(): List<Order> {
        TODO("Not yet implemented")
    }

    override suspend fun getOrderDetails(orderId: String): Order {
        return safeApiCall {
            val response = bookingApiService.getBookingById(orderId)
            if (response.success && response.data != null) {
                response.data.toDomain()
            } else {
                throw Exception(response.message ?: "Order not found or data is null")
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun observeAvailableOrders(): Flow<Order> = flow {
        repeat(3) {
            delay(5_000)
            emit(
                Order(
                    id = Uuid.random().toString(),
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
                        description = "the lamb is brokenjhgjgjgjgjgjgjgjgjgjgjgjjgjgjgjhjgjgjgjgjgjgjgjgjgjgjgjgjgjgjgjgjjhgjgjhggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggg",
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
            )
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun getTechnicianActiveOrder(): Order? {
        return Order(
            id = "",
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
        //  empty
    }

    override suspend fun declineOrder(orderId: String) {
        //  empty
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