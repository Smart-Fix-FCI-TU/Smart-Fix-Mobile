package com.fcitu.smartfix.ui.screen.customer.home

import com.fcitu.smartfix.domain.entity.Order
import com.fcitu.smartfix.domain.model.ServiceCategory
import com.fcitu.smartfix.domain.useCase.GetCustomerOrdersUseCase
import com.fcitu.smartfix.ui.shared.BaseViewModel

class HomeViewModel(private val getCustomerOrdersUseCase: GetCustomerOrdersUseCase) :
    BaseViewModel<HomeUiState, HomeUiEffect>(HomeUiState()), HomeInteractionListener {
    init {
        loadActiveOrders()
    }

    //TODO Create Impl for this function to load customer Info
    fun loadCustomerInfo() {
        TODO("Not yet implemented")
    }

    private fun loadActiveOrders() {
        tryToExecute(
            onStart = ::onGetOrdersStart,
            execute = ::getActiveOrders,
            onSuccess = ::onGetActiveOrderSuccess,
            onError = ::onGetActiveOrdersError
        )
    }

    private fun onGetOrdersStart() {
        updateState { it.copy(isLoadingOrders = true) }
    }

    private suspend fun getActiveOrders(): List<Order> {
        return getCustomerOrdersUseCase.getCustomerActiveOrders()
    }

    private fun onGetActiveOrderSuccess(orders: List<Order>) {
        updateState { it.copy(activeOrders = orders, isLoadingOrders = false) }
    }

    private fun onGetActiveOrdersError(throwable: Throwable) {
        updateState { it.copy(error = throwable.message, isLoadingOrders = false) }
        emitEffect(HomeUiEffect.ShowError(throwable.message ?: "Failed to Load Active Orders"))
    }

    //----------------Listeners----------------------------
    override fun onCategorySelected(category: String) {
        updateState { it.copy(selectedCategory = category) }
    }

    override fun onChooseServiceClicked() {
        val category = state.value.selectedCategory!!
        emitEffect(HomeUiEffect.NavigateToBooking(category))
    }

    override fun onOrderClicked(orderId: String) {
        emitEffect(HomeUiEffect.NavigateToOrderDetails(orderId))
    }

    override fun onSearchClicked() {
        emitEffect(HomeUiEffect.NavigateToSearch)
    }

    override fun onNotificationClicked() {
        emitEffect(HomeUiEffect.NavigateToNotifications)
    }

    override fun onViewAllOrdersClicked(order: List<Order>) {
        emitEffect(HomeUiEffect.NavigateToAllActiveOrders(order))
    }
    //---------------------------------------------------------------
}