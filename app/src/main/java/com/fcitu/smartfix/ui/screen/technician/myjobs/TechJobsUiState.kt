package com.fcitu.smartfix.ui.screen.technician.myjobs

import com.fcitu.smartfix.domain.entity.Order
import com.fcitu.smartfix.domain.entity.User

data class TechJobsUiState(
    val activeJob: Order? = null,
    val historyJobs: List<Order> = emptyList(),
    val customersMap: Map<String, User> = emptyMap(), // orderId → customerName
    val selectedTab: TechJobsTab = TechJobsTab.ACTIVE,
    val isLoadingActive: Boolean = false,
    val isLoadingHistory: Boolean = false,
    val hasNetworkConnection: Boolean = true,
    val error: String? = null,
) {
    val hasActiveJob: Boolean get() = activeJob != null
    val hasHistoryJobs: Boolean get() = historyJobs.isNotEmpty()
}

enum class TechJobsTab { ACTIVE, HISTORY }