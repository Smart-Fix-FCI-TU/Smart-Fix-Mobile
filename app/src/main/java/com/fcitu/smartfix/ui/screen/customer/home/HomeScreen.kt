package com.fcitu.smartfix.ui.screen.customer.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.fcitu.smartfix.ui.designSystem.components.snackBar.LocalSnackBarHostController
import com.fcitu.smartfix.ui.designSystem.components.snackBar.SnackBarData
import com.fcitu.smartfix.ui.screen.customer.home.component.HomeContent
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    onNavigateToBooking: (String) -> Unit = {},
    onNavigateToOrderDetails: (String) -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToAllOrders: () -> Unit = {},
    onNavigateToTechniciansList: (String) -> Unit = {},
    viewModel: HomeViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()

    val snackBarHostController = LocalSnackBarHostController.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is HomeUiEffect.NavigateToBooking ->
                    onNavigateToBooking(effect.selectedCategory)

                is HomeUiEffect.NavigateToOrderDetails ->
                    onNavigateToOrderDetails(effect.orderId)

                is HomeUiEffect.NavigateToNotifications ->
                    onNavigateToNotifications()

                is HomeUiEffect.NavigateToAllActiveOrders ->
                    onNavigateToAllOrders()

                is HomeUiEffect.NavigateToResumePendingOrder -> {
                    onNavigateToTechniciansList(effect.orderId)
                }

                is HomeUiEffect.ShowError -> {
                    snackBarHostController.showSnackBar(
                        snackBarData = SnackBarData(
                            title = "Error",
                            message = effect.message,
                            isError = true,
                            duration = 3000
                        )
                    )
                }

            }
        }
    }
    HomeContent(
        state = state,
        listener = viewModel
    )
}
