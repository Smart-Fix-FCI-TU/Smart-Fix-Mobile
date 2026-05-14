package com.fcitu.smartfix.ui.screen.customer.profile

import com.fcitu.smartfix.ui.shared.BaseViewModel

class CustomerProfileViewModel : BaseViewModel<CustomerProfileUiState, CustomerProfileUiEffect>(
    CustomerProfileUiState()
), CustomerProfileInteractionListener {

    init {
        loadMockProfile()
    }

    private fun loadMockProfile() {
        val mockHistory = (1..10).map { i ->
            ServiceHistoryUiState(
                id = "$i",
                technicianImageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRsynS_f9S3stJ6S6T0V8LUM5VvOa3YqYy1Ag&s",
                serviceName = when(i % 3) {
                    0 -> "Electrician Service"
                    1 -> "Plumbing Service"
                    else -> "Carpentry Service"
                },
                rating = (4.0 + (i % 10) / 10.0).toString().take(3),
                status = "Completed",
                date = "Jan ${10 + i}, 2024"
            )
        }

        updateState {
            it.copy(
                isLoading = false,
                name = "Mahmoud Hassan",
                memberSince = "Member Since: Jan 2023",
                phoneNumber = "+20 123 456 789",
                email = "mahmoud.hassan@email.com",
                address = "9 Maadi St., Cairo",
                profilePhotoUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRsynS_f9S3stJ6S6T0V8LUM5VvOa3YqYy1Ag&s",
                serviceHistory = mockHistory
            )
        }
    }

    override fun onSettingsClicked() {
        emitEffect(CustomerProfileUiEffect.NavigateToSettings)
    }

    override fun onViewAllServiceHistoryClicked() {
        emitEffect(CustomerProfileUiEffect.NavigateToAllServiceHistory)
    }
}
