package com.fcitu.smartfix.ui.screen.technician.home


import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.fcitu.smartfix.R
import com.fcitu.smartfix.ui.designSystem.components.scaffold.Scaffold
import com.fcitu.smartfix.ui.screen.customer.home.component.HomeAppBar
import com.fcitu.smartfix.ui.screen.customer.home.component.NetworkOutageScreen
import com.fcitu.smartfix.ui.screen.technician.home.component.ActiveJobSection
import com.fcitu.smartfix.ui.screen.technician.home.component.AvailabilityToggleCard
import com.fcitu.smartfix.ui.screen.technician.home.component.NewOrdersSection
import com.fcitu.smartfix.ui.screen.technician.home.component.TechnicianHeader
import com.fcitu.smartfix.ui.screen.technician.home.component.orderDetailsSheet
import com.fcitu.smartfix.ui.utils.EffectHandler
import org.koin.androidx.compose.koinViewModel

@Composable
fun TechHomeScreen(
    navController: NavController,
    viewModel: TechHomeViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    LifecycleResumeEffect(Unit) {
        viewModel.onResumed()
        onPauseOrDispose { }
    }
    EffectHandler(effects = viewModel.effect) { effect ->
        when (effect) {
            is TechHomeUiEffect.NavigateToActiveJob -> {}
            is TechHomeUiEffect.NavigateToNotifications -> {}
            is TechHomeUiEffect.ShowError -> {
            }
            is TechHomeUiEffect.ShowToast -> {
                Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    TechHomeContent(
        state = state,
        listener = viewModel,
    )
}

@Composable
fun TechHomeContent(
    state: TechHomeUiState,
    listener: TechHomeInteractionListener,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        backgroundColor = Color(0xFFF2F4F7),
        statusBarColor = Color.Transparent,
        topBar = {
            HomeAppBar(
                "Smart Fix",
                leadingContent = {
                    Image(
                        painter = painterResource(R.drawable.notification_icon),
                        contentDescription = "Notification icon"
                    )
                })
        },
        overlays = {
            orderDetailsSheet(
                state = state,
                isVisible = state.isOrderDetailsVisible, order = state.selectedOrderForSheet,
                onDismiss = { listener.onDismissDetailsSheet() },
                onAcceptClick = {
                    state.selectedOrderForSheet.let { listener.onAcceptOrder(it?.id.toString()) }
                },
                onDelineClick = { state.selectedOrderForSheet.let { listener.onRejectOrder(it?.id.toString()) } }
            )
        }

    ) {
        if (!state.hasNetworkConnection) {
            NetworkOutageScreen(onTryAgain = { listener.onTryAgainClicked() })
            Log.e("Network Outage Screen", "")
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {

            // ── Technician Header ─────────────────────────────────────────────

            state.technician?.let { tech ->
                TechnicianHeader(
                    technician = tech,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }


            // ── Availability Toggle ───────────────────────────────────────────
            AvailabilityToggleCard(
                isAvailable = state.isAvailable,
                canToggle = state.canToggleAvailability,
                onToggle = listener::onToggleAvailability,
                modifier = Modifier.padding(horizontal = 16.dp),
            )

            // ── Active Job Banner (لو عنده job نشط) ──────────────────────────
            if (state.isOnJob && !state.isTransitioning) {
                ActiveJobSection(
                    order = state.acceptedOrder!!,
                    onClick = listener::onContinueActiveJob,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            } else {

                // ── New Orders Section ────────────────────────────────────────────
                NewOrdersSection(
                    state = state,
                    listener = listener,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
        }
    }
}