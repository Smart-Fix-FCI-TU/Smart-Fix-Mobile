package com.fcitu.smartfix.ui.screen.technician.home


sealed interface TechHomeUiEffect {
    data class NavigateToActiveJob(
        val orderId: String,
    ) : TechHomeUiEffect

    data object NavigateToNotifications : TechHomeUiEffect

    data class ShowError(
        val message: String,
    ) : TechHomeUiEffect

    data class ShowToast(
        val message: String,
    ) : TechHomeUiEffect
}