package com.fcitu.smartfix.ui.screen.customer.myorders

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.fcitu.smartfix.domain.entity.Order
import com.fcitu.smartfix.domain.entity.Technician
import com.fcitu.smartfix.domain.repository.TechnicianRepository
import com.fcitu.smartfix.domain.useCase.GetCustomerOrdersUseCase
import com.fcitu.smartfix.ui.shared.BaseViewModel
import com.fcitu.smartfix.ui.utils.InternetConnectionAvailability
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MyOrdersViewModel(
    private val getCustomerOrdersUseCase: GetCustomerOrdersUseCase,
    private val technicianRepository: TechnicianRepository,
    private val networkConnection: InternetConnectionAvailability,
) : BaseViewModel<MyOrdersUiState, MyOrdersUiEffect>(MyOrdersUiState()),
    MyOrdersInteractionListener {

    init {
        observeNetwork()
    }

    // ── Network ───────────────────────────────────────────────────────────────
    private fun observeNetwork() {
        tryToCollect(
            collect = { networkConnection.observeNetworkConnection() },
            onCollect = { isConnected ->
                updateState { it.copy(hasNetworkConnection = isConnected) }
                if (isConnected && !state.value.hasLoaded) {
                    viewModelScope.launch(Dispatchers.Main) {
                        loadOrders()
                    }
                }
            },
            onError = {
                updateState { it.copy(hasNetworkConnection = false) }
            },
        )
    }


    // ── Load Orders ───────────────────────────────────────────────────────────
    private fun loadOrders() {
        updateState {
            it.copy(
                isLoadingActive = true,
                isLoadingHistory = true,
                hasLoaded = false,
            )
        }
        viewModelScope.launch(Dispatchers.Main) {
            kotlinx.coroutines.delay(100)
            loadActiveOrders()
            loadHistoryOrders()
        }
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
                checkIfFullyLoaded()
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
                checkIfFullyLoaded()
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

    private fun checkIfFullyLoaded() {
        val current = state.value
        if (!current.isLoadingActive && !current.isLoadingHistory) {
            updateState { it.copy(hasLoaded = true) }
        }
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
                        Log.e("MyOrdersVM", "Failed for order ${order.id}", e)
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
        emitEffect(MyOrdersUiEffect.NavigateToCompletedOrderDetails(orderId))
    }

    override fun onActiveOrderClicked(orderId: String) {
        emitEffect(MyOrdersUiEffect.NavigateToTrackingActiveOrder(orderId))
    }

    override fun onNotificationClicked() {
        emitEffect(MyOrdersUiEffect.NavigateToNotifications)
    }

    override fun onChatClicked(orderId: String, technicianId: String) {
        emitEffect(MyOrdersUiEffect.NavigateToChat(orderId, technicianId))
    }

    override fun onBackClicked() {
        emitEffect(MyOrdersUiEffect.NavigateBack)
    }
}