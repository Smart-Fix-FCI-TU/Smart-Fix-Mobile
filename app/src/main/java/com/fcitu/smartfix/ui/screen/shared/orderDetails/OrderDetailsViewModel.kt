package com.fcitu.smartfix.ui.screen.shared.orderDetails

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.fcitu.smartfix.domain.entity.Order
import com.fcitu.smartfix.domain.repository.OrderRepository
import com.fcitu.smartfix.ui.navigation.Route
import com.fcitu.smartfix.ui.shared.BaseViewModel

class OrderDetailsViewModel(
    private val orderRepository: OrderRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<OrderDetailsUiState, OrderDetailsEffect>(OrderDetailsUiState()),
    OrderDetailsInteractionListener {


    init {
        val orderId = savedStateHandle.toRoute<Route.OrderDetail>().orderId
        updateState { it.copy(orderId = orderId) }
        loadOrderDetails(orderId)
    }

    override fun onBackClicked() {
        emitEffect(OrderDetailsEffect.NavigateBack)
    }

    override fun onRateTechnicianClicked() {
        updateState { it.copy(showRatingBottomSheet = true) }
    }

    override fun onSubmitRatingClicked(rating: Float, comment: String) {
        TODO("Not yet implemented")
    }

    override fun onDismissRatingBottomSheetClicked() {
        updateState { it.copy(showRatingBottomSheet = false) }
    }

    override fun onGoHomeClicked() {
        emitEffect(OrderDetailsEffect.NavigateToHomeScreen)
    }

    private fun loadOrderDetails(orderId: String) {
        tryToExecute(
            onStart = ::onLoadOrderDetailsStart,
            execute = { onLoadOrderDetails(orderId) },
            onSuccess = ::onLoadOrderDetailsSuccess,
            onError = ::onLoadOrderDetailsError
        )
    }

    private fun onLoadOrderDetailsStart() {
        updateState { it.copy(isLoading = true) }
    }

    private suspend fun onLoadOrderDetails(orderId: String): Order {
        return orderRepository.getOrderDetails(orderId)
    }

    private fun onLoadOrderDetailsSuccess(order: Order) {
        updateState {
            it.copy(
                isLoading = false,
                title = order.details.title,
                description = order.details.description,
                address = order.details.address,
                problemPhotoUrls = order.details.problemPhotoUrls,
                timeline = order.timeline,
                repairPhotos = order.repairPhotos,
            )
        }
    }


    // TODO: Handle different error types (Network, Server, etc.) later.
    // TODO: Add retry mechanism for network errors, and show appropriate messages for different error types.
    private fun onLoadOrderDetailsError(error: Throwable) {
        updateState { it.copy(isLoading = false, error = error.message) }
        emitEffect(
            OrderDetailsEffect.ShowSnackBar(
                error.message ?: "Failed to Load Order Details", isError = true
            )
        )
    }
}