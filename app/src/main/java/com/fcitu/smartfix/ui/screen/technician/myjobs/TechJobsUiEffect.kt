package com.fcitu.smartfix.ui.screen.technician.myjobs

sealed interface TechJobsUiEffect {
    data class NavigateToActiveJob(val orderId: String) : TechJobsUiEffect
    data class NavigateToJobDetails(val orderId: String) : TechJobsUiEffect
    data class NavigateToChat(val orderId: String, val customerId: String) : TechJobsUiEffect
    data class NavigateToTheDialerApp(val phoneNumber: String) : TechJobsUiEffect
    data class ShowError(val message: String) : TechJobsUiEffect
}