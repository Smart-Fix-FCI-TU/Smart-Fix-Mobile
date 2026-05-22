package com.fcitu.smartfix.ui.screen.technician.myjobs

import com.fcitu.smartfix.domain.entity.Order
import com.fcitu.smartfix.domain.entity.User
import com.fcitu.smartfix.domain.repository.CustomerRepository
import com.fcitu.smartfix.domain.repository.OrderRepository
import com.fcitu.smartfix.ui.shared.BaseViewModel
import com.fcitu.smartfix.ui.utils.InternetConnectionAvailability
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope


class TechJobsViewModel(
    private val customerRepository: CustomerRepository,
    private val orderRepository: OrderRepository,
    private val networkConnection: InternetConnectionAvailability,
) : BaseViewModel<TechJobsUiState, TechJobsUiEffect>(TechJobsUiState()),
    TechJobsInteractionListener {

    init {
        loadJobs()
    }

    // ── Network ───────────────────────────────────────────────────────────────
    private fun requireNetwork(action: () -> Unit) {
        if (!networkConnection.isNetworkAvailable()) {
            updateState { it.copy(hasNetworkConnection = false) }
            emitEffect(TechJobsUiEffect.ShowError("No Internet Connection"))
            return
        }
        updateState { it.copy(hasNetworkConnection = true) }
        action()
    }

    // ── Load ──────────────────────────────────────────────────────────────────
    private fun loadJobs() = requireNetwork {
        loadActiveJob()
        loadHistoryJobs()
    }

    private fun loadActiveJob() {
        tryToExecute(
            onStart = { updateState { it.copy(isLoadingActive = true) } },
            execute = {
                val order = orderRepository.getTechnicianActiveOrder()
                val customerMap = fetchCustomerInfoForOrders(listOf(order!!))
                Pair(order, customerMap)

            },
            onSuccess = { (activeOrder, customerMap) ->
                updateState {
                    it.copy(
                        customersMap = it.customersMap + customerMap,
                        activeJob = activeOrder, isLoadingActive = false
                    )
                }
            },
            onError = {error->
                updateState { it.copy(activeJob = null, isLoadingActive = false) }
                emitEffect(
                    TechJobsUiEffect.ShowError(
                        error.message ?: "Failed to load the active Job"
                    )
                )
            },
        )
    }

    private fun loadHistoryJobs() {
        tryToExecute(
            onStart = { updateState { it.copy(isLoadingHistory = true) } },
            execute = {
                val orders = orderRepository.getOrdersByStatus("completed")
                val customerMap = fetchCustomerInfoForOrders(orders)
                Pair(orders, customerMap)

            },
            onSuccess = { (completedOrders, customerMap) ->
                updateState {
                    it.copy(
                        customersMap = it.customersMap + customerMap,
                        historyJobs = completedOrders, isLoadingHistory = false
                    )
                }
            },
            onError = { error ->
                updateState { it.copy(isLoadingHistory = false, error = error.message) }
                emitEffect(
                    TechJobsUiEffect.ShowError(
                        error.message ?: "Failed to load Completed jobs"
                    )
                )
            },
        )
    }

    private suspend fun fetchCustomerInfoForOrders(orders: List<Order>): Map<String, User> {
        return coroutineScope {
            orders.map { order ->
                async {
                    try {
                        val customer = customerRepository.getCustomerById(order.customer.id)
                        order.id to customer
                    } catch (e: Exception) {
                        null
                    }
                }

            }.awaitAll().filterNotNull().toMap()
        }
    }

    // ── Listeners ─────────────────────────────────────────────────────────────
    override fun onTabSelected(tab: TechJobsTab) = requireNetwork {
        updateState { it.copy(selectedTab = tab) }
    }

    override fun onActiveJobClicked(orderId: String) = requireNetwork {
        emitEffect(TechJobsUiEffect.NavigateToActiveJob(orderId))
    }

    override fun onChatClicked(orderId: String, customerId: String) = requireNetwork {
        emitEffect(TechJobsUiEffect.NavigateToChat(orderId, customerId))
    }

    override fun onDialerClicked(phoneNumber: String) = requireNetwork {
        emitEffect(TechJobsUiEffect.NavigateToTheDialerApp(phoneNumber))
    }

    override fun onTryAgainClick() = requireNetwork {
        loadJobs()
    }

    override fun onHistoryJobClicked(orderId: String) = requireNetwork {
        emitEffect(TechJobsUiEffect.NavigateToJobDetails(orderId))
    }


}