package com.fcitu.smartfix.ui.screen.customer.profile

sealed interface CustomerProfileUiEffect {
    data object NavigateBack : CustomerProfileUiEffect
    data object NavigateToSettings : CustomerProfileUiEffect
    data object NavigateToAllServiceHistory : CustomerProfileUiEffect
    data class ShowSnackBar(val message: String, val isError: Boolean) : CustomerProfileUiEffect
}
