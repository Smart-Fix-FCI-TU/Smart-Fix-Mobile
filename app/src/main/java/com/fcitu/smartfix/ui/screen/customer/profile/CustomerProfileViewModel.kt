package com.fcitu.smartfix.ui.screen.customer.profile

import com.fcitu.smartfix.ui.shared.BaseViewModel

class CustomerProfileViewModel : BaseViewModel<CustomerProfileUiState, CustomerProfileUiEffect>(
    CustomerProfileUiState()
), CustomerProfileInteractionListener {

    init {
        loadMockProfile()
    }

    private fun loadMockProfile() {
        updateState {
            it.copy(
                isLoading = false,
                name = "Mahmoud Hassan",
                memberSince = "Member Since: Jan 2023",
                phoneNumber = "+20 123 456 789",
                email = "mahmoud.hassan@email.com",
                address = "9 Maadi St., Cairo",
                profilePhotoUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRsynS_f9S3stJ6S6T0V8LUM5VvOa3YqYy1Ag&s",
                isOnline = true,
                serviceHistory = listOf(
                    ServiceHistoryUiState(
                        id = "1",
                        technicianImageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRsynS_f9S3stJ6S6T0V8LUM5VvOa3YqYy1Ag&s",
                        serviceName = "Electrician Service",
                        rating = "4.9",
                        status = "Completed",
                        date = "Jan 15, 2024",
                        isTechnicianOnline = true
                    ),
                    ServiceHistoryUiState(
                        id = "2",
                        technicianImageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRsynS_f9S3stJ6S6T0V8LUM5VvOa3YqYy1Ag&s",
                        serviceName = "Plumbing Service",
                        rating = "4.8",
                        status = "Completed",
                        date = "Dec 10, 2023",
                        isTechnicianOnline = true
                    )
                )
            )
        }
    }

    override fun onBackClicked() {
        emitEffect(CustomerProfileUiEffect.NavigateBack)
    }

    override fun onSettingsClicked() {
        emitEffect(CustomerProfileUiEffect.NavigateToSettings)
    }

    override fun onViewAllServiceHistoryClicked() {
        emitEffect(CustomerProfileUiEffect.NavigateToAllServiceHistory)
    }
}
