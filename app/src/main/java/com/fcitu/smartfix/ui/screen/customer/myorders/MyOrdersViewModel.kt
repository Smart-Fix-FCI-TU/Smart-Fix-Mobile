package com.fcitu.smartfix.ui.screen.customer.myorders

import com.fcitu.smartfix.domain.useCase.GetCustomerOrdersUseCase
import com.fcitu.smartfix.ui.shared.BaseViewModel
import com.fcitu.smartfix.ui.utils.InternetConnectionAvailability

class MyOrdersViewModel(
    private val getCustomerOrdersUseCase: GetCustomerOrdersUseCase,
    private val networkConnection: InternetConnectionAvailability,
): BaseViewModel<MyOrdersUiState, MyOrdersUiEffect>(MyOrdersUiState()),
    MyOrdersInteractionListener {


    init {
        observeNetwork()
        loadOrders()
    }

    private fun loadOrders() {
        TODO("Not yet implemented")
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
        TODO("Not yet implemented")
    }

    override fun onCompletedOrderClicked(orderId: String) {
        TODO("Not yet implemented")
    }

    override fun onActiveOrderClicked(orderId: String) {
        TODO("Not yet implemented")
    }

    override fun onNotificationClicked() {
        TODO("Not yet implemented")
    }

    override fun onBackClicked() {
        TODO("Not yet implemented")
    }
}