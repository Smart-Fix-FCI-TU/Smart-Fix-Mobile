package com.fcitu.smartfix.ui.screen.customer.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.fcitu.smartfix.R
import com.fcitu.smartfix.ui.designSystem.components.scaffold.Scaffold
import com.fcitu.smartfix.ui.screen.customer.home.component.HomeAppBar
import com.fcitu.smartfix.ui.screen.customer.home.component.NetworkOutageScreen
import com.fcitu.smartfix.ui.screen.customer.home.component.OrderSection
import com.fcitu.smartfix.ui.screen.customer.home.component.PendingOrderSection
import com.fcitu.smartfix.ui.screen.customer.home.component.ServicesSection
import com.fcitu.smartfix.ui.screen.customer.home.component.UserProfileHeader
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

    HomeEffectsHandler(
        effects = viewModel.effect,
        onNavigateToBooking = onNavigateToBooking,
        onNavigateToOrderDetails = onNavigateToOrderDetails,
        onNavigateToNotifications = onNavigateToNotifications,
        onNavigateToAllOrders = onNavigateToAllOrders,
        onNavigateToTechniciansList = onNavigateToTechniciansList
    )

    HomeContent(
        state = state,
        listener = viewModel
    )
}

@Composable
private fun HomeContent(
    state: HomeUiState,
    listener: HomeInteractionListener,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        backgroundColor = Color(0xFFF2F4F7),
        statusBarColor = Color.Transparent,
        topBar = {
            HomeAppBar(title = "Smart Fix", leadingContent = {
                Image(
                    painter = painterResource(R.drawable.notification_icon),
                    contentDescription = "",
                    modifier = Modifier.size(20.dp)
                )
            }, onLeadingClick = { listener.onNotificationClicked() })
        }
    ) {
        if (state.hasNetworkConnection) {
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(state = scrollState),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {

                UserProfileHeader(state.userState, Modifier.padding(horizontal = 16.dp))

                if (state.hasPendingOrder) {
                    PendingOrderSection(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        orderId = state.pendingOrderId!! // هنا الـ !! آمنة عشان الشرط اللي فوق بيمنع إنها تكون null
                    ) {
                        listener.onNavigateToAvailableTechnicianList(state.pendingOrderId)
                    }
                }

                OrderSection(state, listener, Modifier.padding(horizontal = 16.dp))
                ServicesSection(state, listener, Modifier.padding(horizontal = 16.dp))
            }
        } else {
            NetworkOutageScreen()
        }
    }
}