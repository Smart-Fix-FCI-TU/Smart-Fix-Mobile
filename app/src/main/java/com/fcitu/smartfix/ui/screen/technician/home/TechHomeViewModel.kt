package com.fcitu.smartfix.ui.screen.technician.home

import androidx.lifecycle.viewModelScope
import com.fcitu.smartfix.domain.entity.Order
import com.fcitu.smartfix.domain.repository.OrderRepository
import com.fcitu.smartfix.domain.repository.TechnicianRepository
import com.fcitu.smartfix.ui.shared.BaseViewModel
import com.fcitu.smartfix.ui.utils.InternetConnectionAvailability
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch


class TechHomeViewModel(
    private val technicianRepository: TechnicianRepository,
    private val orderRepository: OrderRepository,
    private val networkConnection: InternetConnectionAvailability,
) : BaseViewModel<TechHomeUiState, TechHomeUiEffect>(TechHomeUiState()),
    TechHomeInteractionListener {

    private var ordersObserverJob: Job? = null

    init {
        loadTechnicianInfo()
    }

    fun onResumed() {
        loadTechnicianInfo()
    }

    // ─── Network ──────────────────────────────────────────────────────────────

    private fun requireNetwork(action: () -> Unit) {
        if (!networkConnection.isNetworkAvailable()) {
            updateState { it.copy(hasNetworkConnection = false) }
            emitEffect(TechHomeUiEffect.ShowError("No Internet Connection"))
            return
        }
        updateState { it.copy(hasNetworkConnection = true) }
        action()
    }

    // ─── Technician Info ──────────────────────────────────────────────────────

    private fun loadTechnicianInfo() = requireNetwork {
        tryToExecute(
            onStart = { updateState { it.copy(isLoadingTechnicianInfo = true) } },
            execute = { technicianRepository.getMyProfile() },
            onSuccess = { technician ->
                updateState {
                    it.copy(
                        canToggleAvailability = !technician.isOnJob,
                        isLoadingTechnicianInfo = false,
                        technician = technician,
                        isAvailable = technician.isAvailable,
                        isOnJob = technician.isOnJob,
                    )
                }
                handleOrderObservingAfterProfileLoad(isOnJob = technician.isOnJob)
                loadActiveJob()
            },
            onError = { error ->
                updateState { it.copy(isLoadingTechnicianInfo = false, error = error.message) }
            }
        )
    }

    private fun handleOrderObservingAfterProfileLoad(isOnJob: Boolean) {
        if (!isOnJob) {
            if (ordersObserverJob?.isActive != true) startObservingOrders()
        } else {
            stopObservingOrders()
        }
    }

    // ─── Active Job ───────────────────────────────────────────────────────────

    private fun loadActiveJob() {
        tryToExecute(
            onStart = { updateState { it.copy(isLoadingActiveOrder = true) } },
            execute = { orderRepository.getTechnicianActiveOrder() },
            onSuccess = { order ->
                updateState { it.copy(acceptedOrder = order, isLoadingActiveOrder = false) }
                warnIfActiveJobMissing()
            },
            onError = {
                updateState { it.copy(acceptedOrder = null, isLoadingActiveOrder = false) }
            }
        )
    }

    private fun warnIfActiveJobMissing() {
        if (state.value.isOnJob && state.value.acceptedOrder == null) {
            emitEffect(TechHomeUiEffect.ShowError("Failed to load active job details."))
        }
    }

    // ─── Orders Observer ──────────────────────────────────────────────────────

    private fun startObservingOrders() {
        if (ordersObserverJob?.isActive == true) return

        ordersObserverJob = viewModelScope.launch {
            orderRepository.observeAvailableOrders()
                .onEach { newOrder ->
                    handleIncomingOrder(newOrder)
                }
                .launchIn(this)
        }
    }

    private fun handleIncomingOrder(newOrder: Order) {
        val isNotRejected = newOrder.id !in state.value.rejectedOrderIds
        val isAvailable = state.value.isAvailable
        if (!isNotRejected || !isAvailable) return

        updateState {
            val alreadyExists = it.pendingOrders.any { order -> order.id == newOrder.id }
            if (alreadyExists) it
            else it.copy(
                pendingOrders = it.pendingOrders + newOrder,
            )
        }
    }

    private fun stopObservingOrders() {
        ordersObserverJob?.cancel()
        ordersObserverJob = null
    }

    // ─── Availability Toggle ──────────────────────────────────────────────────

    override fun onToggleAvailability(isAvailable: Boolean) {
        if (isAvailable && state.value.isOnJob) {
            emitEffect(TechHomeUiEffect.ShowToast("Complete your current job first"))
            return
        }
        if (!isAvailable && !state.value.isOnJob && state.value.pendingOrders.isNotEmpty()) {
            handleAutoRejectOnToggleOff()
            return
        }
        toggleAvailabilityOnServer(isAvailable)
    }

    private fun toggleAvailabilityOnServer(isAvailable: Boolean) {
        tryToExecute(
            onStart = {
                updateState {
                    it.copy(
                        isTogglingAvailability = true,
                    )
                }
            },
            execute = { technicianRepository.toggleAvailability(isAvailable) },
            onSuccess = { newStatus ->
                updateState { it.copy(isAvailable = newStatus, isTogglingAvailability = false) }
                if (newStatus) startObservingOrders() else stopObservingOrders()
            },
            onError = { error ->
                updateState { it.copy(isTogglingAvailability = false) }
                emitEffect(TechHomeUiEffect.ShowError(error.message ?: "Failed to update status"))
            }
        )
    }

    private fun handleAutoRejectOnToggleOff() {
        val pendingOrderIds = state.value.pendingOrders.map { it.id }.toSet()
        if (pendingOrderIds.isEmpty()) return

        markAllPendingAsRejected(pendingOrderIds)
        stopObservingOrders()
        declineOrdersInBackground(pendingOrderIds)
        clearRejectedOrdersWithDelay()
    }

    private fun markAllPendingAsRejected(orderIds: Set<String>) {
        updateState {
            it.copy(
                rejectedOrderIds = orderIds,
                isAvailable = false,
                isTogglingAvailability = true,
            )
        }
    }

    private fun clearRejectedOrdersWithDelay() {
        viewModelScope.launch {
            delay(2_000)
            updateState { it.copy(pendingOrders = emptyList()) }

            delay(500)
            updateState { it.copy(rejectedOrderIds = emptySet(), isTogglingAvailability = false) }
        }
    }

    // ─── Accept Order ─────────────────────────────────────────────────────────

    override fun onAcceptOrder(orderId: String) = requireNetwork {
        tryToExecute(
            execute = { orderRepository.acceptOrder(orderId = orderId) },
            onSuccess = { handleOrderAccepted(orderId) },
            onError = { error ->
                emitEffect(TechHomeUiEffect.ShowError(error.message ?: "Failed to accept order"))
            },
        )
    }

    private fun handleOrderAccepted(acceptedId: String) {
        val acceptedOrder = state.value.pendingOrders.find { it.id == acceptedId }
        val otherOrderIds = state.value.pendingOrders
            .filter { it.id != acceptedId }
            .map { it.id }
            .toSet()

        updateStateForAcceptedOrder(acceptedOrder, acceptedId, otherOrderIds)
        stopObservingOrders()
        declineOrdersInBackground(otherOrderIds)
        runAcceptanceAnimationSequence(acceptedId, acceptedOrder)
    }

    private fun updateStateForAcceptedOrder(
        acceptedOrder: Order?,
        acceptedId: String,
        otherOrderIds: Set<String>
    ) {
        updateState {
            it.copy(
                canToggleAvailability = false,
                acceptedOrder = acceptedOrder,
                acceptedOrderId = acceptedId,
                rejectedOrderIds = otherOrderIds,
                isAvailable = false,
            )
        }
    }

    private fun runAcceptanceAnimationSequence(acceptedId: String, acceptedOrder: Order?) {
        viewModelScope.launch {
            delay(2_000)
            // Remove rejected orders, keep only accepted → triggers exit animation
            updateState { it.copy(pendingOrders = it.pendingOrders.filter { o -> o.id == acceptedId }) }

            delay(2_000)
            // Final cleanup after exit animation completes
            updateState {
                it.copy(
                    pendingOrders = emptyList(),
                    isOnJob = true,
                    acceptedOrderId = null,
                    rejectedOrderIds = emptySet(),
                )
            }
            acceptedOrder?.let { emitEffect(TechHomeUiEffect.NavigateToActiveJob(it.id)) }
        }
    }

    // ─── Reject Order ─────────────────────────────────────────────────────────

    override fun onRejectOrder(orderId: String) {
        markOrderAsRejected(orderId)
        declineOrdersInBackground(setOf(orderId))
        removeRejectedOrderWithDelay(orderId)
    }

    private fun markOrderAsRejected(orderId: String) {
        updateState { it.copy(rejectedOrderIds = it.rejectedOrderIds + orderId) }
    }

    private fun removeRejectedOrderWithDelay(orderId: String) {
        viewModelScope.launch {
            delay(2_500)
            updateState { it.copy(pendingOrders = it.pendingOrders.filter { o -> o.id != orderId }) }

            delay(500)
            updateState { it.copy(rejectedOrderIds = it.rejectedOrderIds - orderId) }
        }
    }

    // ─── Order Timeout ────────────────────────────────────────────────────────

    override fun onOrderTimeout(orderId: String) {
        markOrderAsRejected(orderId)
        declineOrdersInBackground(setOf(orderId))
        removeTimedOutOrderWithDelay(orderId)
    }

    private fun removeTimedOutOrderWithDelay(orderId: String) {
        viewModelScope.launch {
            delay(3_000)
            updateState { it.copy(pendingOrders = it.pendingOrders.filter { o -> o.id != orderId }) }

            delay(500)
            updateState { it.copy(rejectedOrderIds = it.rejectedOrderIds - orderId) }
        }
    }

    // ─── Shared Helpers ───────────────────────────────────────────────────────

    private fun declineOrdersInBackground(orderIds: Set<String>) {
        viewModelScope.launch {
            orderIds.forEach { id ->
                runCatching { orderRepository.declineOrder(id) }
            }
        }
    }

    // ─── Order Details Sheet ──────────────────────────────────────────────────

    override fun onViewOrderDetails(order: Order) = requireNetwork {
        updateState { it.copy(selectedOrderForSheet = order, isOrderDetailsVisible = true) }
    }

    override fun onDismissDetailsSheet() {
        updateState { it.copy(selectedOrderForSheet = null, isOrderDetailsVisible = false) }
    }

    // ─── Navigation & Misc ───────────────────────────────────────────────────

    override fun onNotificationClicked() = requireNetwork {
        emitEffect(TechHomeUiEffect.NavigateToNotifications)
    }

    override fun onContinueActiveJob() = requireNetwork {
        state.value.acceptedOrder?.let { emitEffect(TechHomeUiEffect.NavigateToActiveJob(it.id)) }
    }

    override fun onTryAgainClicked() = requireNetwork {
        loadTechnicianInfo()
    }

    override fun onCleared() {
        super.onCleared()
        ordersObserverJob?.cancel()
    }
}