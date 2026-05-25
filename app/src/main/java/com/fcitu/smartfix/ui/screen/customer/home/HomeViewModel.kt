package com.fcitu.smartfix.ui.screen.customer.home

import com.fcitu.smartfix.R
import com.fcitu.smartfix.domain.entity.Order
import com.fcitu.smartfix.domain.entity.ServiceItem
import com.fcitu.smartfix.domain.entity.User
import com.fcitu.smartfix.domain.model.ServiceCategory
import com.fcitu.smartfix.domain.repository.CustomerRepository
import com.fcitu.smartfix.domain.useCase.GetCustomerOrdersUseCase
import com.fcitu.smartfix.ui.shared.BaseViewModel
import com.fcitu.smartfix.ui.utils.InternetConnectionAvailability

class HomeViewModel(
    private val getCustomerOrdersUseCase: GetCustomerOrdersUseCase,
    private val networkConnection: InternetConnectionAvailability,
    private val customerRepository: CustomerRepository
) :
    BaseViewModel<HomeUiState, HomeUiEffect>(HomeUiState()), HomeInteractionListener {


    init {
        loadHomeScreen()
    }

    private fun loadHomeScreen() {
        if (!networkConnection.isNetworkAvailable()) {
            updateState { it.copy(hasNetworkConnection = false) }
            emitEffect(HomeUiEffect.ShowError("No Internet Connection"))
            return
        }

        loadServicesList()
        loadCustomerInfo()
        loadActiveOrders()
        loadPendingOrder()
    }

    //Loading Services List
    private fun loadServicesList() {
        updateState {
            it.copy(
                hasNetworkConnection = true,
                servicesList = listOf(
                    ServiceItem(
                        serviceCategory = ServiceCategory.ELECTRICITY,
                        serviceName = "Electricity",
                        R.drawable.electricity_icon_active,
                        R.drawable.electricity_icon
                    ),
                    ServiceItem(
                        serviceCategory = ServiceCategory.PLUMBING,
                        "Plumbing",
                        R.drawable.plumbing_icon_active,
                        R.drawable.plumbing_icon
                    ),
                    ServiceItem(
                        serviceCategory = ServiceCategory.CONDITIONING,
                        "Conditioning",
                        R.drawable.conditioning_icon_active,
                        R.drawable.conditioning_icon
                    ),
                    ServiceItem(
                        serviceCategory = ServiceCategory.PAINTING,
                        "Paints",
                        R.drawable.painting_icon_active,
                        R.drawable.painting_icon
                    ),
                    ServiceItem(
                        serviceCategory = ServiceCategory.CARPENTRY,
                        "Carpentry",
                        R.drawable.carpentry_icon_active,
                        R.drawable.carpentry_icon
                    ),

                    )
            )
        }
    }

    //Loading User Information
    fun loadCustomerInfo() {
        tryToExecute(
            onStart = ::onGetUserInfoStart,
            execute = ::getUserInfo,
            onSuccess = ::onGetUserInfoSuccess,
            onError = ::onGetUserInfoError
        )
    }

    private fun onGetUserInfoStart() {
        updateState { it.copy(userState = UserProfileState.Loading) }
    }

    private fun onGetUserInfoSuccess(user: User) {
        updateState { it.copy(userState = UserProfileState.Success(user)) }
    }

    private fun onGetUserInfoError(throwable: Throwable) {
        updateState {
            it.copy(
                userState = UserProfileState.Error(throwable.message ?: "Failed")
            )
        }
        emitEffect(HomeUiEffect.ShowError(throwable.message ?: "Failed to Load User Info"))

    }

    private suspend fun getUserInfo(): User {
        return customerRepository.getMyProfile()
    }
    //------------------------------------------------------------------------------------


    //Loading Active Orders---------------------------------------------------------------------------------
    private fun loadActiveOrders() {
        tryToExecute(
            onStart = ::onGetOrdersStart,
            execute = ::getActiveOrders,
            onSuccess = ::onGetActiveOrderSuccess,
            onError = ::onGetActiveOrdersError
        )
    }

    private fun onGetOrdersStart() {
        updateState { it.copy(isLoadingActiveOrders = true) }
    }

    private suspend fun getActiveOrders(): List<Order> {
        return getCustomerOrdersUseCase.getCustomerActiveOrders()
    }

    private fun onGetActiveOrderSuccess(orders: List<Order>) {
        updateState { it.copy(activeOrders = orders, isLoadingActiveOrders = false) }
    }

    private fun onGetActiveOrdersError(throwable: Throwable) {
        updateState { it.copy(error = throwable.message, isLoadingActiveOrders = false) }
        emitEffect(HomeUiEffect.ShowError(throwable.message ?: "Failed to Load Active Orders"))
    }
//-------------------------------------------------------------------------------------------------------

    // Loading Pending Order----------------------------------------------------------------------------
    private fun onStartLoadingPendingOrder() {
        updateState { it.copy(isLoadingPendingOrder = true) }

    }

    private suspend fun getPendingOrder(): String? {
        return getCustomerOrdersUseCase.getCustomerPendingRequest()?.id
    }

    private fun onGetPendingOrdersSuccess(orderId: String?) {
        updateState { it.copy(isLoadingPendingOrder = false, pendingOrderId = orderId) }
    }

    private fun onGetPendingOrderError(throwable: Throwable) {
        updateState {
            it.copy(
                isLoadingPendingOrder = false,
                error = throwable.message ?: "Failed to Load Pending Order"
            )
        }
        emitEffect(HomeUiEffect.ShowError(throwable.message ?: "Failed to Load Pending Order"))
    }

    private fun loadPendingOrder() {
        tryToExecute(
            onStart = ::onStartLoadingPendingOrder,
            execute = ::getPendingOrder,
            onSuccess = ::onGetPendingOrdersSuccess,
            onError = ::onGetPendingOrderError


        )
    }

    //---------------------------------------------------------------------------------------------


    //--------------------------------------------------------------------

    //----------------Listeners----------------------------
    override fun onCategorySelected(category: String) {
        updateState { it.copy(selectedCategory = if (category == it.selectedCategory) null else category) }
    }

    override fun onChooseServiceClicked() {
        val category = state.value.selectedCategory ?: return
        emitEffect(HomeUiEffect.NavigateToBooking(category))
    }

    override fun onOrderClicked(orderId: String) {
        emitEffect(HomeUiEffect.NavigateToTrackingOrder(orderId))
    }

    override fun onNotificationClicked() {
        emitEffect(HomeUiEffect.NavigateToNotifications)
    }

    override fun onViewAllOrdersClicked(orders: List<Order>) {
        emitEffect(HomeUiEffect.NavigateToAllActiveOrders(orders))
    }

    override fun onNavigateToAvailableTechnicianList(orderId: String) {
        emitEffect(HomeUiEffect.NavigateToAvailableTechnicianList(orderId))
    }

    override fun onTryAgainClicked() {
        loadHomeScreen()
    }
    //---------------------------------------------------------------
}