package com.fcitu.smartfix.ui.screen.customer.myorders

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.fcitu.smartfix.ui.navigation.Route
import com.fcitu.smartfix.ui.utils.EffectHandler
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun MyOrdersEffectsHandler(
    effects: SharedFlow<MyOrdersUiEffect>,
    navController: NavController,
    onNavigateBack: () -> Unit = {},
) {
    //TODO: Navigate to the rest of the screens when there are finished.
    EffectHandler(effects = effects) { effect ->
        when (effect) {
            is MyOrdersUiEffect.NavigateBack -> onNavigateBack()
            is MyOrdersUiEffect.NavigateToChat -> {}
            is MyOrdersUiEffect.NavigateToCompletedOrderDetails -> {
                navController.navigate(Route.OrderDetail(orderId = effect.orderId))
            }
            is MyOrdersUiEffect.NavigateToNotifications -> {}
            is MyOrdersUiEffect.NavigateToTrackingActiveOrder -> {}
            is MyOrdersUiEffect.ShowError -> {}
        }

    }
}