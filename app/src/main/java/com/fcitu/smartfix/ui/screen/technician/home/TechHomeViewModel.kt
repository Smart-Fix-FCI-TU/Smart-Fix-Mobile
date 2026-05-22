package com.fcitu.smartfix.ui.screen.technician.home

import android.util.Log
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

    // Network Check
    private fun requireNetwork(action: () -> Unit) {
        if (!networkConnection.isNetworkAvailable()) {
            updateState { it.copy(hasNetworkConnection = false) }
            emitEffect(TechHomeUiEffect.ShowError("No Internet Connection"))
            return
        }
        updateState { it.copy(hasNetworkConnection = true) }
        action()
    }

    //--------------------------------------------------------------------
    private fun loadTechnicianInfo() = requireNetwork {
        tryToExecute(
            onStart = { updateState { it.copy(isLoadingTechnicianInfo = true) } },
            execute = { technicianRepository.getMyProfile() },
            onSuccess = { technician ->
                updateState {
                    it.copy(
                        isLoadingOrders = true,
                        canToggleAvailability = !technician.isOnJob,
                        isLoadingTechnicianInfo = false,
                        technician = technician,
                        isAvailable = technician.isAvailable,
                        isOnJob = technician.isOnJob,
                    )
                }
                if (!technician.isOnJob) {
                    if (ordersObserverJob?.isActive != true) {
                        startObservingOrders()
                    }
                } else {
                    stopObservingOrders()
                }
                loadActiveJob()
            },
            onError = { error ->
                updateState {
                    it.copy(
                        isLoadingTechnicianInfo = false,
                        error = error.message
                    )
                }
            }
        )
    }

    private fun loadActiveJob() {
        tryToExecute(
            onStart = { updateState { it.copy(isLoadingActiveOrder = true) } },
            execute = { orderRepository.getTechnicianActiveOrder() },
            onSuccess = { order ->
                updateState {
                    it.copy(
                        acceptedOrder = order,
                        isLoadingActiveOrder = false
                    )
                }
                if (state.value.isOnJob && state.value.acceptedOrder == null) {
                    emitEffect(TechHomeUiEffect.ShowError("Failed to load active job details."))
                }
            },
            onError = {
                updateState {
                    it.copy(
                        acceptedOrder = null,
                        isLoadingActiveOrder = false
                    )
                }
            }
        )
    }

    private fun startObservingOrders() {
        if (ordersObserverJob?.isActive == true) return

        ordersObserverJob = viewModelScope.launch {
            orderRepository.observeAvailableOrders()
                .onStart {
                    updateState { it.copy(isLoadingOrders = false) }
                }
                .onEach { newOrder ->
                    if (newOrder.id !in state.value.rejectedOrderIds
                        && state.value.isAvailable
                    ) {
                        updateState {
                            //  We make sure the order isn't already on the list so it doesn't get duplicated.
                            if (it.pendingOrders.none { order -> order.id == newOrder.id }) {
                                it.copy(
                                    pendingOrders = it.pendingOrders + newOrder,
                                    isLoadingOrders = false
                                )
                            } else {
                                it // If the order exists, revert the state to its original state without modification.
                            }
                        }
                    }
                }.launchIn(this)
        }
    }

    private fun stopObservingOrders() {
        ordersObserverJob?.cancel()
        ordersObserverJob = null
    }

    override fun onToggleAvailability(isAvailable: Boolean) {
        if (isAvailable && state.value.isOnJob) {
            emitEffect(TechHomeUiEffect.ShowToast("Complete your current job first"))
            return
        }

        if (!isAvailable && !state.value.isOnJob && state.value.pendingOrders.isNotEmpty()) {
            handleAutoRejectOnToggleOff()
            return
        }

        tryToExecute(
            onStart = { updateState { it.copy(isTogglingAvailability = true) } },
            execute = { technicianRepository.toggleAvailability(isAvailable) },
            onSuccess = { newStatus ->
                updateState {
                    it.copy(
                        isAvailable = newStatus,
                        isTogglingAvailability = false
                    )
                }
                if (newStatus) {
                    startObservingOrders()
                } else {
                    stopObservingOrders()
                }
            },
            onError = { error ->
                updateState { it.copy(isTogglingAvailability = false) }
                emitEffect(TechHomeUiEffect.ShowError(error.message ?: "Failed to update status"))
            }
        )
    }

    private fun handleAutoRejectOnToggleOff() {
        Log.e("Handle Auto Reject", "")
        val pendingOrders = state.value.pendingOrders
        if (pendingOrders.isEmpty()) return

        val pendingOrderIds = pendingOrders.map { it.id }.toSet()

        updateState {
            it.copy(
                rejectedOrderIds = pendingOrderIds,
                isAvailable = false,
                isTogglingAvailability = true,
            )
        }

        stopObservingOrders()

        viewModelScope.launch {
            pendingOrderIds.forEach { orderId ->
                try {
                    orderRepository.declineOrder(orderId)
                } catch (e: Exception) {
                }
            }
        }

        viewModelScope.launch {
            delay(2_000)

            updateState {
                it.copy(pendingOrders = emptyList())
            }

            delay(500)

            updateState {
                it.copy(
                    rejectedOrderIds = emptySet(),
                    isTogglingAvailability = false,
                )
            }
        }
    }

    override fun onAcceptOrder(orderId: String) = requireNetwork {
        tryToExecute(
            execute = { orderRepository.acceptOrder(orderId = orderId) },
            onSuccess = {
                val acceptedOrder = state.value.pendingOrders.find { it.id == orderId }
                val otherOrdersIds = state.value.pendingOrders
                    .filter { it.id != orderId }
                    .map { it.id }
                    .toSet()

                //  Update UI immediately - show accepted (green) & rejected (red)
                updateState {
                    it.copy(
                        canToggleAvailability = false,
                        acceptedOrder = acceptedOrder,
                        acceptedOrderId = orderId,
                        rejectedOrderIds = otherOrdersIds, // Keep rejected IDs set
                        isAvailable = false,
                    )
                }

                stopObservingOrders()

                viewModelScope.launch {
                    otherOrdersIds.forEach { rejectedId ->
                        try {
                            orderRepository.declineOrder(rejectedId)
                        } catch (e: Exception) {
                        }
                    }
                }

                //  Handle animations & cleanup with correct timing
                viewModelScope.launch {
                    // Wait for user to see rejected state (2s)
                    delay(2_000)

                    // Remove rejected orders from list → triggers exit animation completion
                    // Keep only the accepted order in the list
                    updateState {
                        it.copy(
                            pendingOrders = it.pendingOrders.filter { order -> order.id == orderId }
                        )
                    }

                    // Wait for exit animation to complete (2s)
                    delay(2_000)

                    //  Final cleanup - NOW safe to clear rejected IDs
                    updateState {
                        it.copy(
                            pendingOrders = emptyList(),
                            isOnJob = true,
                            acceptedOrderId = null,
                            rejectedOrderIds = emptySet(), // Clear after removal
                        )
                    }

                    // Navigate to active job screen
                    acceptedOrder?.let { order ->
                        emitEffect(TechHomeUiEffect.NavigateToActiveJob(order.id))
                    }
                }
            },
            onError = { error ->
                emitEffect(TechHomeUiEffect.ShowError(error.message ?: "Failed to accept order"))
            },
        )
    }

    override fun onRejectOrder(orderId: String) {
        //  Mark as rejected visually immediately
        updateState { it.copy(rejectedOrderIds = it.rejectedOrderIds + orderId) }

        viewModelScope.launch {
            try {
                orderRepository.declineOrder(orderId)
            } catch (e: Exception) {
            }

            //  Show rejected state for 2-3 seconds
            delay(2_500)

            //  Remove from list (triggers exit animation)
            updateState {
                it.copy(
                    pendingOrders = it.pendingOrders.filter { o -> o.id != orderId }
                )
            }

            //  Wait for animation, then cleanup
            delay(500)
            updateState {
                it.copy(rejectedOrderIds = it.rejectedOrderIds - orderId)
            }
        }
    }

    override fun onViewOrderDetails(order: Order) = requireNetwork {
        updateState {
            it.copy(
                selectedOrderForSheet = order,
                isOrderDetailsVisible = true
            )
        }
    }

    override fun onDismissDetailsSheet() {
        updateState {
            it.copy(
                selectedOrderForSheet = null,
                isOrderDetailsVisible = false
            )
        }
    }

    override fun onNotificationClicked() = requireNetwork {
        emitEffect(TechHomeUiEffect.NavigateToNotifications)
    }

    override fun onContinueActiveJob() {
        if (!networkConnection.isNetworkAvailable()) {
            updateState { it.copy(hasNetworkConnection = false) }
            emitEffect(TechHomeUiEffect.ShowError("No Internet Connection"))
            return
        }
        state.value.acceptedOrder?.let { emitEffect(TechHomeUiEffect.NavigateToActiveJob(it.id)) }
    }

    override fun onOrderTimeout(orderId: String) {
        updateState { it.copy(rejectedOrderIds = it.rejectedOrderIds + orderId) }

        viewModelScope.launch {
            try {
                orderRepository.declineOrder(orderId)
            } catch (e: Exception) {
            }

            delay(3_000) // Requirement: 3 seconds

            updateState {
                it.copy(
                    pendingOrders = it.pendingOrders.filter { o -> o.id != orderId }
                )
            }

            delay(500)
            updateState {
                it.copy(rejectedOrderIds = it.rejectedOrderIds - orderId)
            }
        }
    }

    override fun onTryAgainClicked() = requireNetwork {
        loadTechnicianInfo()
    }

    override fun onCleared() {
        super.onCleared()
        ordersObserverJob?.cancel()

    }
}