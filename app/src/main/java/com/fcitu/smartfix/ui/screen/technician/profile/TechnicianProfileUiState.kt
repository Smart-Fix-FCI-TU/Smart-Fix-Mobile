package com.fcitu.smartfix.ui.screen.technician.profile
data class TechnicianProfileUiState(
    val name: String = "",
    val occupation: String = "",
    val location: String = "",
    val rating: String = "0.0",
    val reviewCount: String = "0",
    val experience: String = "0 Years",
    val bio: String = "",
    val reviews: List<ReviewUiState> = emptyList(),
    val profilePhotoUrl: String = "",
    val isOnline: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)

data class ReviewUiState(
    val id: String = "",
    val reviewerName: String = "",
    val rating: Int = 0,
    val comment: String = "",
    val date: String = ""
)
