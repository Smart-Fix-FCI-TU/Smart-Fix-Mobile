package com.fcitu.smartfix.ui.screen.shared.orderDetails

interface OrderDetailsEffect {
    object NavigateBack : OrderDetailsEffect
    object NavigateToHomeScreen : OrderDetailsEffect
    data class ShowSnackBar(val message: String, val isError: Boolean) : OrderDetailsEffect
}