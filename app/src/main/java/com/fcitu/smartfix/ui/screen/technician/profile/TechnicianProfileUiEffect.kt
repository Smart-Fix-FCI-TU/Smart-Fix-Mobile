package com.fcitu.smartfix.ui.screen.technician.profile

sealed interface TechnicianProfileUiEffect {
    data object NavigateBack : TechnicianProfileUiEffect
    data object NavigateToSettings : TechnicianProfileUiEffect
    data object NavigateToAllReviews : TechnicianProfileUiEffect
    data class ShowSnackBar(val message: String, val isError: Boolean) : TechnicianProfileUiEffect
}
