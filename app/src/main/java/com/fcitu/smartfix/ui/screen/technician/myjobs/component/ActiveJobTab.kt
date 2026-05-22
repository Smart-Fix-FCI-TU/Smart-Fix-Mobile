package com.fcitu.smartfix.ui.screen.technician.myjobs.component


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fcitu.smartfix.ui.screen.technician.myjobs.TechJobsInteractionListener
import com.fcitu.smartfix.ui.screen.technician.myjobs.TechJobsUiState

@Composable
fun ActiveJobTab(
    state: TechJobsUiState,
    listener: TechJobsInteractionListener,
    modifier: Modifier
) {
    when {
        state.isLoadingActive -> {
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 3.dp,
                    trackColor = Color(0xFFFF4A08)
                )
            }
        }

        !state.hasActiveJob -> {
            JobsEmptyState(
                title = "No Active Job",
                subtitle = "The current Job that has not yet been completed will appear here."
            )
        }

        state.hasActiveJob -> {
            if (state.activeJob != null) {

                Column(
                    modifier = modifier.padding(16.dp).fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val activeOrder = state.activeJob
                    ActiveJobCard(
                        activeOrder = activeOrder,
                        user = state.customersMap[activeOrder.id],
                        onActiveJobClicked = { listener.onActiveJobClicked(activeOrder.id) },
                        onChatClicked = { activeOrderId, customerId ->
                            listener.onChatClicked(
                                orderId = activeOrderId,
                                customerId
                            )
                        },
                        onDialerClicked = { phoneNumber ->
                            listener.onDialerClicked(phoneNumber)
                        }

                    )
                }
            }
        }
    }

}
