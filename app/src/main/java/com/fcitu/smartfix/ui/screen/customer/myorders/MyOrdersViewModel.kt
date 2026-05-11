package com.fcitu.smartfix.ui.screen.customer.myorders

import com.fcitu.smartfix.domain.entity.Technician
import com.fcitu.smartfix.domain.repository.TechnicianRepository
import com.fcitu.smartfix.domain.useCase.GetCustomerOrdersUseCase
import com.fcitu.smartfix.ui.shared.BaseViewModel
import com.fcitu.smartfix.ui.utils.InternetConnectionAvailability

class MyOrdersViewModel(
    private val getCustomerOrdersUseCase: GetCustomerOrdersUseCase,
    private val technicianRepository: TechnicianRepository,
    private val networkConnection: InternetConnectionAvailability,
) : BaseViewModel<MyOrdersUiState, MyOrdersUiEffect>(MyOrdersUiState()),
    MyOrdersInteractionListener {


    init {
        observeNetwork()
        loadOrders()
    }


    //── Load Orders ───────────────────────────────────────────────────────────────
    private fun loadOrders() {
        loadActiveOrders()
        loadHistoryOrders()
    }

    private fun loadHistoryOrders() {
        tryToExecute(
            onStart = { updateState { it.copy(isLoadingHistory = true) } },
            execute = {
                getCustomerOrdersUseCase.getCustomerCompletedOrders()
            },
            onSuccess = { completedOrders ->
                updateState {
                    it.copy(
                        isLoadingHistory = false,
                        historyOrders = completedOrders,
                        error = null
                    )
                }
            },
            onError = { error ->
                updateState {
                    it.copy(
                        error = error.message ?: "Failed to Loading History Orders"
                    )
                }
                emitEffect(
                    MyOrdersUiEffect.ShowError(
                        error.message ?: "Failed to Loading History Orders"
                    )
                )
            }
        )

    }

    private fun loadActiveOrders() {
        tryToExecute(
            onStart = { updateState { it.copy(isLoadingActive = true) } },
            execute = { getCustomerOrdersUseCase.getCustomerActiveOrders() },
            onSuccess = { orders ->
                updateState {
                    it.copy(
                        activeOrders = orders,
                        isLoadingActive = false,
                        error = null,
                    )
                }
            },
            onError = { error ->
                updateState { it.copy(isLoadingActive = false, error = error.message) }
                emitEffect(MyOrdersUiEffect.ShowError(error.message ?: "Failed to load orders"))
            })
    }


    // ── Network ───────────────────────────────────────────────────────────────
    private fun observeNetwork() {
        tryToCollect(
            collect = { networkConnection.observeNetworkConnection() },
            onCollect = { isConnected ->
                updateState { it.copy(hasNetworkConnection = isConnected) }
                if (isConnected) loadOrders()
            },
            onError = {
                updateState { it.copy(hasNetworkConnection = false) }
            },
        )
    }

    //Load
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

    override fun onBackClicked() {
        emitEffect(MyOrdersUiEffect.NavigateBack)
    }

    override fun onGetTechnicianInfo(technicianId: String): Technician? {
        var technician: Technician? = null
       tryToExecute(
            execute = { technicianRepository.getTechnicianDetails(technicianId = technicianId) },
           onSuccess = { technician = it}
        )
        return technician
    }

}