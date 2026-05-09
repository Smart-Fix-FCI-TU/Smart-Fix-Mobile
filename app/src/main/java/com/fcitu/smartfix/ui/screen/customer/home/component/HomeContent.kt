package com.fcitu.smartfix.ui.screen.customer.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.fcitu.smartfix.R
import com.fcitu.smartfix.ui.designSystem.components.scaffold.Scaffold
import com.fcitu.smartfix.ui.designSystem.components.snackBar.AnimatedSnackBarHost
import com.fcitu.smartfix.ui.designSystem.components.snackBar.LocalSnackBarHostController
import com.fcitu.smartfix.ui.screen.customer.home.HomeInteractionListener
import com.fcitu.smartfix.ui.screen.customer.home.HomeUiState

@Composable
fun HomeContent(
    state: HomeUiState,
    listener: HomeInteractionListener,
) {
    val snackBarHostController = LocalSnackBarHostController.current
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
        },
        snakeBar = {
            AnimatedSnackBarHost(
                snackBarHostController = snackBarHostController,
                modifier = Modifier.padding(top = 60.dp)
            )
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

                if (state.user != null) {
                    UserProfileHeader(state.user, Modifier.padding(horizontal = 16.dp))
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                if (state.hasPendingOrder) {
                    PendingOrderSection(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        orderId = state.pendingOrderId!! // هنا الـ !! آمنة عشان الشرط اللي فوق بيمنع إنها تكون null
                    ) {
                        listener.onResumePendingOrderClicked(state.pendingOrderId)
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