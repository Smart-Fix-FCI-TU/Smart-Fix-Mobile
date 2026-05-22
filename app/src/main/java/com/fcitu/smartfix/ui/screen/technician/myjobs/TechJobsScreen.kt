package com.fcitu.smartfix.ui.screen.technician.myjobs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.fcitu.smartfix.ui.designSystem.components.appBar.AppBar
import com.fcitu.smartfix.ui.designSystem.components.scaffold.Scaffold
import com.fcitu.smartfix.ui.navigation.Route
import com.fcitu.smartfix.ui.screen.customer.home.component.NetworkOutageScreen
import com.fcitu.smartfix.ui.screen.technician.myjobs.component.ActiveJobTab
import com.fcitu.smartfix.ui.screen.technician.myjobs.component.HistoryJobsTab
import com.fcitu.smartfix.ui.screen.technician.myjobs.component.TechJobsTabRow
import com.fcitu.smartfix.ui.utils.EffectHandler
import com.fcitu.smartfix.ui.utils.makePhoneCall
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TechJobsScreen(
    navController: NavController,
    viewModel: TechJobsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    EffectHandler(effects = viewModel.effect) { effect ->
        when (effect) {
            is TechJobsUiEffect.NavigateToActiveJob -> {}
            is TechJobsUiEffect.NavigateToJobDetails -> {
                navController.navigate(Route.OrderDetail(effect.orderId))
            }

            is TechJobsUiEffect.NavigateToChat -> {}
            is TechJobsUiEffect.ShowError -> {}
            is TechJobsUiEffect.NavigateToTheDialerApp -> {
                context.makePhoneCall(effect.phoneNumber)
            }
        }
    }
    TechJobsContent(state = state, listener = viewModel)
}

@Composable
private fun TechJobsContent(
    state: TechJobsUiState,
    listener: TechJobsInteractionListener
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        backgroundColor = Color(0xFFF2F4F7),
        statusBarColor = Color.Transparent,
        topBar = {
            AppBar(title = "My Jobs")
        }
    ) {
        if (!state.hasNetworkConnection) {
            NetworkOutageScreen(onTryAgain = { listener.onTryAgainClick() })
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()

            ) {
                TechJobsTabRow(
                    state.selectedTab,
                    onTabClicked = { selectedTab -> listener.onTabSelected(selectedTab) })
                when (state.selectedTab) {
                    TechJobsTab.ACTIVE -> {
                        ActiveJobTab(state, listener, modifier = Modifier.weight(1f))
                    }

                    TechJobsTab.HISTORY -> {
                        HistoryJobsTab(
                            state, listener,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }

}