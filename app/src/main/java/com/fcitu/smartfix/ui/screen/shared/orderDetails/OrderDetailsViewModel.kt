package com.fcitu.smartfix.ui.screen.shared.orderDetails

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.fcitu.smartfix.domain.entity.Order
import com.fcitu.smartfix.domain.entity.Review
import com.fcitu.smartfix.domain.repository.OrderRepository
import com.fcitu.smartfix.domain.repository.ReviewRepository
import com.fcitu.smartfix.ui.navigation.Route
import com.fcitu.smartfix.ui.shared.BaseViewModel

class OrderDetailsViewModel(
    private val orderRepository: OrderRepository,
    private val reviewRepository: ReviewRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<OrderDetailsUiState, OrderDetailsEffect>(OrderDetailsUiState()),
    OrderDetailsInteractionListener {


    init {
        val orderId = savedStateHandle.toRoute<Route.OrderDetail>().orderId
        updateState { it.copy(orderId = orderId) }
        loadOrderDetails(orderId)
        fetchExistingReview(orderId)
    }

    override fun onBackClicked() {
        emitEffect(OrderDetailsEffect.NavigateBack)
    }

    override fun onRateTechnicianClicked() {
        updateState { it.copy(showRatingBottomSheet = true) }
    }

    override fun onSubmitRatingClicked(rating: Int, comment: String, chips: List<String>) {
        val fullComment = if (chips.isNotEmpty()) {
            "${chips.joinToString(", ")}. $comment"
        } else {
            comment
        }

        tryToExecute(
            onStart = ::onSubmitRatingStart,
            execute = { onSubmitRating(rating, fullComment) },
            onSuccess = { onSubmitRatingSuccess(rating, fullComment) },
            onError = ::onSubmitRatingError
        )
    }

    private fun onSubmitRatingStart() {
        updateState { it.copy(isSubmittingRating = true) }
    }

    private suspend fun onSubmitRating(rating: Int, fullComment: String) {
        reviewRepository.submitReview(
            orderId = state.value.orderId,
            rating = rating,
            comment = fullComment
        )
    }

    private fun onSubmitRatingSuccess(rating: Int, comment: String) {
        updateState {
            it.copy(
                isRatingSuccess = true,
                isSubmittingRating = false,
                rating = rating,
                comment = comment,
            )
        }
    }

    private fun onSubmitRatingError(error: Throwable) {
        updateState { it.copy(isSubmittingRating = false) }
        emitEffect(
            OrderDetailsEffect.ShowSnackBar(
                error.message ?: "Failed to Submit Rating", isError = true
            )
        )
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
                technicianId = order.technician.id,
                title = order.details.title,
                description = order.details.description,
                address = order.details.address,
                problemPhotoUrls = order.details.problemPhotoUrls,
                timeLine = order.timeline,
                repairPhotos = order.repairPhotos,
            )
        }
    }

    private fun fetchExistingReview(orderId: String) {
        tryToExecute(
            execute = { onGetExistingReview(orderId) },
            onSuccess = ::onGetExistingReviewSuccess
        )
    }

    private suspend fun onGetExistingReview(orderId: String): Review? {
        return reviewRepository.getReviewByBookingId(orderId)
    }

    private suspend fun onGetExistingReviewSuccess(review: Review?) {
        review?.let {
            updateState {
                it.copy(
                    rating = review.rating,
                    comment = review.comment
                )
            }
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