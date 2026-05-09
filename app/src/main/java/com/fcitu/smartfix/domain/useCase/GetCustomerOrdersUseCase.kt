package com.fcitu.smartfix.domain.useCase

import com.fcitu.smartfix.domain.entity.Order
import com.fcitu.smartfix.domain.model.isActive
import com.fcitu.smartfix.domain.model.isCompleted
import com.fcitu.smartfix.domain.model.isPendingRequest
import com.fcitu.smartfix.domain.repository.OrderRepository

class GetCustomerOrdersUseCase(private val orderRepository: OrderRepository) {
    suspend fun getCustomerActiveOrders(): List<Order> {
        return orderRepository.getCustomerOrders().filter { order -> order.status.isActive }
    }

    suspend fun getCustomerCompletedOrders(): List<Order> {
        return orderRepository.getCustomerOrders().filter { order ->
            order.status.isCompleted
        }
    }

    suspend fun getCustomerPendingRequest():Order?{
        return orderRepository.getCustomerOrders().find { order ->
            order.status.isPendingRequest
        }
    }
}