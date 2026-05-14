package com.fcitu.smartfix.ui.screen.customer.techniciansList

import com.fcitu.smartfix.domain.entity.Technician

data class TechnicianListUiState(
    val selectedFilter: FilterType = FilterType.ALL,
    val technicians: List<Technician> = emptyList(),
    val selectedTechnician: Technician? = null,
    val isTechnicianProfileSheetVisible: Boolean = false,
    val isWaitingTechnicianSheetVisible: Boolean = false,
    val isAcceptedSheetVisible: Boolean = false,
    val isRejectedSheetVisible: Boolean = false,
    val isLoading: Boolean = false,
    val countdownTime: String = "02:00"
)


enum class FilterType {
    ALL,
    TOP_RATED,
    NEAREST
}