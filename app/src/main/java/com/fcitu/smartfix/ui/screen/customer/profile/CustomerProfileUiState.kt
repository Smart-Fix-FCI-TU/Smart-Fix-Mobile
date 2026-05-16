package com.fcitu.smartfix.ui.screen.customer.profile

data class CustomerProfileUiState(
    val name: String = "",
    val memberSince: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val address: String = "",
    val profilePhotoUrl: String = "",
    val serviceHistory: List<ServiceHistoryUiState> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

data class ServiceHistoryUiState(
    val id: String = "",
    val technicianImageUrl: String = "",
    val serviceName: String = "",
    val rating: String = "",
    val status: String = "",
    val date: String = ""
)
