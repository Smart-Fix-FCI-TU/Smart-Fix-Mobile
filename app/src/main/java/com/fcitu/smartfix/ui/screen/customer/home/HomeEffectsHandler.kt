package com.fcitu.smartfix.ui.screen.customer.home

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.fcitu.smartfix.ui.navigation.Route
import com.fcitu.smartfix.ui.utils.EffectHandler
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun HomeEffectsHandler(
    effects: SharedFlow<HomeUiEffect>,
    navController: NavController
) {

    //TODO: Navigate to the rest of the screens when there are finished.
    EffectHandler(effects = effects) { effect ->
        when (effect) {
            is HomeUiEffect.NavigateToBooking -> {}//Booking
            is HomeUiEffect.NavigateToOrderDetails -> {}//Tracking
            is HomeUiEffect.NavigateToNotifications -> {}//Notification
            is HomeUiEffect.NavigateToAllActiveOrders -> {
                navController.navigate(Route.CustomerOrders)
            }
            is HomeUiEffect.NavigateToAvailableTechnicianList -> {}
            is HomeUiEffect.ShowError -> {

            }
        }
    }
}