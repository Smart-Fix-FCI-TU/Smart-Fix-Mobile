package com.fcitu.smartfix.ui.screen.customer.myorders

import androidx.compose.runtime.Composable
import com.fcitu.smartfix.ui.utils.EffectHandler
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun MyOrdersEffectsHandler(
    effects: SharedFlow<MyOrdersUiEffect>,
    onNavigateToCompletedOrderDetails: (String) -> Unit,
    onNavigateToTrackingActiveOrderDetails: (String) -> Unit,
    onNavigateToChat: (String, String) -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateBack: () -> Unit,
) {
    EffectHandler(effects = effects) { effect ->
        when (effect) {
            is MyOrdersUiEffect.NavigateBack -> onNavigateBack()
            is MyOrdersUiEffect.NavigateToChat -> onNavigateToChat(
                effect.orderId,
                effect.technicianId
            )

            is MyOrdersUiEffect.NavigateToCompletedOrderDetails -> onNavigateToCompletedOrderDetails(
                effect.orderId
            )

            is MyOrdersUiEffect.NavigateToNotifications -> onNavigateToNotifications()
            is MyOrdersUiEffect.NavigateToTrackingActiveOrder -> onNavigateToTrackingActiveOrderDetails(
                effect.orderId
            )

            is MyOrdersUiEffect.ShowError -> {}
        }

    }
}