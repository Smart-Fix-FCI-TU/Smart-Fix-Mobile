package com.fcitu.smartfix.ui.screen.customer.home

import androidx.compose.runtime.Composable
import com.fcitu.smartfix.ui.utils.EffectHandler
import kotlinx.coroutines.flow.SharedFlow

@Composable
 fun HomeEffectsHandler(
    effects: SharedFlow<HomeUiEffect>,
    onNavigateToBooking: (String) -> Unit,
    onNavigateToOrderDetails: (String) -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToAllOrders: () -> Unit,
    onNavigateToTechniciansList: (String) -> Unit
) {

    EffectHandler(effects = effects) { effect ->
        when (effect) {
            is HomeUiEffect.NavigateToBooking -> {
                onNavigateToBooking(effect.selectedCategory)
            }

            is HomeUiEffect.NavigateToOrderDetails -> {
                onNavigateToOrderDetails(effect.orderId)
            }

            is HomeUiEffect.NavigateToNotifications -> {
                onNavigateToNotifications()
            }

            is HomeUiEffect.NavigateToAllActiveOrders -> {
                onNavigateToAllOrders()
            }

            is HomeUiEffect.NavigateToAvailableTechnicianList -> {
                onNavigateToTechniciansList(effect.orderId)
            }

            is HomeUiEffect.ShowError -> {

            }
        }
    }
}