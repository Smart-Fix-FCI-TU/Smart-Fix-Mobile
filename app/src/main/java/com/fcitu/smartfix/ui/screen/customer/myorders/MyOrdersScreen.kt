package com.fcitu.smartfix.ui.screen.customer.myorders

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.fcitu.smartfix.ui.designSystem.components.appBar.AppBar
import com.fcitu.smartfix.ui.designSystem.components.scaffold.Scaffold
import com.fcitu.smartfix.ui.screen.customer.home.component.NetworkOutageScreen
import com.fcitu.smartfix.ui.screen.customer.myorders.component.ActiveOrdersTab
import com.fcitu.smartfix.ui.screen.customer.myorders.component.HistoryOrdersTab
import com.fcitu.smartfix.ui.screen.customer.myorders.component.OrdersTabRow
import org.koin.androidx.compose.koinViewModel

@Composable
fun MyOrdersScreen(
    navController: NavController,
    viewModel: MyOrdersViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    MyOrdersEffectsHandler(
        effects = viewModel.effect,
        navController = navController,
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
            AppBar(title = "My Orders")
        }
    ) {
        if (!state.hasNetworkConnection) {
            NetworkOutageScreen(onTryAgain = { listener.onTryAgainClicked() })
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
                        HistoryOrdersTab(
                            state, listener,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}