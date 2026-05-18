package com.fcitu.smartfix.ui.screen.customer.myorders

import com.fcitu.smartfix.domain.entity.Order
import com.fcitu.smartfix.domain.entity.Technician
import com.fcitu.smartfix.domain.repository.TechnicianRepository
import com.fcitu.smartfix.domain.useCase.GetCustomerOrdersUseCase
import com.fcitu.smartfix.ui.shared.BaseViewModel
import com.fcitu.smartfix.ui.utils.InternetConnectionAvailability
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class MyOrdersViewModel(
    private val getCustomerOrdersUseCase: GetCustomerOrdersUseCase,
    private val technicianRepository: TechnicianRepository,
    private val networkConnection: InternetConnectionAvailability,
) : BaseViewModel<MyOrdersUiState, MyOrdersUiEffect>(MyOrdersUiState()),
    MyOrdersInteractionListener {

    init {
        loadOrders()
    }

    fun refreshOrders() {
        loadOrders()
    }


    // ── Load Orders ───────────────────────────────────────────────────────────
    private fun loadOrders() {
        val isCurrentlyLoading = state.value.isLoadingActive || state.value.isLoadingHistory
        if (isCurrentlyLoading) return

        if (!networkConnection.isNetworkAvailable()) {
            updateState {
                it.copy(
                    hasNetworkConnection = false,
                    error = "No internet connection. Please check your network.",
                    isLoadingActive = false,
                    isLoadingHistory = false
                )
            }
            emitEffect(MyOrdersUiEffect.ShowError("No internet connection"))
            return
        }

        updateState {
            it.copy(
                hasNetworkConnection = true,
                isLoadingActive = true,
                isLoadingHistory = true,
                technicianMap = emptyMap()
            )
        }
        loadActiveOrders()
        loadHistoryOrders()

    }

    private fun loadActiveOrders() {
        tryToExecute(
            execute = {
                val orders = getCustomerOrdersUseCase.getCustomerActiveOrders()
                val technicianMap = fetchTechnicianForOrders(orders)
                Pair(orders, technicianMap)
            },
            onSuccess = { (orders, techMap) ->
                updateState {
                    it.copy(
                        activeOrders = orders,
                        technicianMap = it.technicianMap + techMap,
                        isLoadingActive = false,
                        error = null,
                    )
                }
            },
            onError = { error ->
                updateState { it.copy(isLoadingActive = false, error = error.message) }
                emitEffect(MyOrdersUiEffect.ShowError(error.message ?: "Failed to load orders"))
            }
        )
    }

    private fun loadHistoryOrders() {
        tryToExecute(
            execute = {
                val orders = getCustomerOrdersUseCase.getCustomerCompletedOrders()
                val techMap = fetchTechnicianForOrders(orders)
                Pair(orders, techMap)
            },
            onSuccess = { (completedOrders, techMap) ->
                updateState {
                    it.copy(
                        isLoadingHistory = false,
                        historyOrders = completedOrders,
                        technicianMap = it.technicianMap + techMap,
                        error = null
                    )
                }
            },
            onError = { error ->
                updateState {
                    it.copy(
                        isLoadingHistory = false,
                        error = error.message ?: "Failed to load history"
                    )
                }
                emitEffect(MyOrdersUiEffect.ShowError(error.message ?: "Failed to load history"))
            }
        )
    }


    // ── Fetch Technicians ─────────────────────────────────────────────────────
    private suspend fun fetchTechnicianForOrders(orders: List<Order>): Map<String, Technician> {
        return coroutineScope {
            orders.map { order ->
                async {
                    try {
                        val tech = technicianRepository.getTechnicianDetails(order.technician.id)
                        order.id to tech
                    } catch (e: Exception) {
                        null
                    }
                }
            }
                .awaitAll()
                .filterNotNull()
                .toMap()
        }
    }

    // ── Listeners ─────────────────────────────────────────────────────────────
    override fun onTabSelected(tab: OrdersTab) {
        updateState { it.copy(selectedTab = tab) }
    }

    override fun onCompletedOrderClicked(orderId: String) {
        if (!networkConnection.isNetworkAvailable()) {
            updateState { it.copy(hasNetworkConnection = false) }
            emitEffect(MyOrdersUiEffect.ShowError("No internet connection"))
            return
        }
        emitEffect(MyOrdersUiEffect.NavigateToCompletedOrderDetails(orderId))
    }

    override fun onActiveOrderClicked(orderId: String) {
        if (!networkConnection.isNetworkAvailable()) {
            updateState { it.copy(hasNetworkConnection = false) }
            emitEffect(MyOrdersUiEffect.ShowError("No internet connection"))
            return
        }
        emitEffect(MyOrdersUiEffect.NavigateToTrackingActiveOrder(orderId))
    }

    override fun onChatClicked(orderId: String, technicianId: String) {
        emitEffect(MyOrdersUiEffect.NavigateToChat(orderId, technicianId))
    }

    override fun onTryAgainClicked() {
        refreshOrders()
    }
}