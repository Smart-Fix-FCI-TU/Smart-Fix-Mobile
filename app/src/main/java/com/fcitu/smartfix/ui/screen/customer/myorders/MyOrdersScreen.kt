package com.fcitu.smartfix.ui.screen.customer.myorders

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fcitu.smartfix.R
import com.fcitu.smartfix.ui.designSystem.components.scaffold.Scaffold
import com.fcitu.smartfix.ui.screen.customer.home.component.NetworkOutageScreen
import com.fcitu.smartfix.ui.screen.customer.myorders.component.ActiveOrdersTab
import com.fcitu.smartfix.ui.screen.customer.myorders.component.HistoryOrdersTab
import com.fcitu.smartfix.ui.screen.customer.myorders.component.MyOrdersAppBar
import com.fcitu.smartfix.ui.screen.customer.myorders.component.OrdersTabRow
import org.koin.androidx.compose.koinViewModel

@Composable
fun MyOrdersScreen(
    onNavigateToCompletedOrderDetails: (String) -> Unit = {},
    onNavigateToTrackingActiveOrder: (String) -> Unit = {},
    onNavigateToChat: (String, String) -> Unit = { _, _ -> },
    onNavigateToNotifications: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    viewModel: MyOrdersViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LifecycleResumeEffect(Unit) {
        viewModel.refreshOrders()
        onPauseOrDispose {}
    }

    MyOrdersEffectsHandler(
        effects = viewModel.effect,
        onNavigateBack = onNavigateBack,
        onNavigateToCompletedOrderDetails = onNavigateToCompletedOrderDetails,
        onNavigateToNotifications = onNavigateToNotifications,
        onNavigateToTrackingActiveOrderDetails = onNavigateToTrackingActiveOrder,
        onNavigateToChat = onNavigateToChat

    )
    MyOrdersContent(state, listener = viewModel)

}

@Composable
private fun MyOrdersContent(
    state: MyOrdersUiState,
    listener: MyOrdersInteractionListener,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        backgroundColor = Color(0xFFF2F4F7),
        statusBarColor = Color.Transparent,
        topBar = {
            MyOrdersAppBar(
                title = "My Orders",
                onLeadingClick = { listener.onBackClicked() },
                leadingContent = {
                    Image(
                        painter = painterResource(R.drawable.left_arrow_icon),
                        contentDescription = "Left Arrow Icon"
                    )
                },
                trailingContent = {
                    Image(
                        painter = painterResource(R.drawable.notification_icon),
                        contentDescription = "Notification Icon"
                    )
                },
                onTrailingClick = { listener.onNotificationClicked() }
            )
        }
    ) {
        if (!state.hasNetworkConnection) {
            NetworkOutageScreen()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()

            ) {
            OrdersTabRow(
                state.selectedTab,
                onTabClicked = { selectedTab -> listener.onTabSelected(selectedTab) })
            when (state.selectedTab) {
                OrdersTab.ACTIVE -> {
                    ActiveOrdersTab(state, listener, modifier = Modifier.weight(1f))
                }

                OrdersTab.HISTORY -> {
                    HistoryOrdersTab(state, listener,
                        modifier = Modifier.weight(1f))
                }
            }
        }}
    }
}