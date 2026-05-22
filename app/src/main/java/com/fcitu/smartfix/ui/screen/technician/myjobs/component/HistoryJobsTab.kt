package com.fcitu.smartfix.ui.screen.technician.myjobs.component


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fcitu.smartfix.ui.screen.technician.myjobs.TechJobsInteractionListener
import com.fcitu.smartfix.ui.screen.technician.myjobs.TechJobsUiState

@Composable
fun HistoryJobsTab(
    state: TechJobsUiState,
    listener: TechJobsInteractionListener,
    modifier: Modifier
) {
    when {
        state.isLoadingHistory -> {
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

        !state.hasHistoryJobs -> {
            JobsEmptyState(
                "No Past Jobs", "All successfully completed Jobs will appear here."
            )
        }

        state.hasHistoryJobs -> {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(items = state.historyJobs, key = { it.id }) { completedOrder ->
                    CompletedJobCard(
                        order = completedOrder,
                        user = state.customersMap[completedOrder.id],
                        onCompletedOrderClicked = { listener.onHistoryJobClicked(orderId = completedOrder.id) },
                    )
                }
            }
        }

    }
}