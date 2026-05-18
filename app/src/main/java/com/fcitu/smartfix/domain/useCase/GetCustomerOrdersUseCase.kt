package com.fcitu.smartfix.domain.useCase

import com.fcitu.smartfix.domain.entity.Order
import com.fcitu.smartfix.domain.repository.OrderRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class GetCustomerOrdersUseCase(private val orderRepository: OrderRepository) {
    suspend fun getCustomerActiveOrders(): List<Order> = coroutineScope {
        val accepted = async { orderRepository.getOrdersByStatus("accepted") }
        val started = async { orderRepository.getOrdersByStatus("started") }
        (accepted.await() + started.await()).sortedByDescending {
            it.timeline.createdAt.toString()
        }
    }
    suspend fun getCustomerCompletedOrders(): List<Order> = coroutineScope {
        val completed = async { orderRepository.getOrdersByStatus("completed") }
        val cancelled = async { orderRepository.getOrdersByStatus("cancelled") }
        val rejected  = async { orderRepository.getOrdersByStatus("rejected") }
        (completed.await() + cancelled.await() + rejected.await()).sortedByDescending {
            it.timeline.createdAt.toString()
        }
    }

    suspend fun getCustomerPendingRequest():Order?{
        return orderRepository.getActivePendingOrder()
    }
}