package com.fcitu.smartfix.ui.screen.technician.profile

import com.fcitu.smartfix.domain.entity.Review
import com.fcitu.smartfix.domain.entity.Technician
import com.fcitu.smartfix.domain.repository.TechnicianRepository
import com.fcitu.smartfix.ui.shared.BaseViewModel

class TechnicianProfileViewModel(
    private val technicianRepository: TechnicianRepository
) : BaseViewModel<TechnicianProfileUiState, TechnicianProfileUiEffect>(
    TechnicianProfileUiState()
), TechnicianProfileInteractionListener {

    init {
        // Mock data directly since the server is down
        loadMockProfile()
    }

    private fun loadMockProfile() {
        updateState {
            it.copy(
                isLoading = false,
                name = "Mahmoud Hassan",
                occupation = "Electrician",
                location = "Maadi, Cairo",
                rating = "4.8",
                reviewCount = "124",
                experience = "5 Years",
                bio = "Professional electrician with 5 years of experience in residential and commercial maintenance. Specialized in diagnostics and new installations. Fully licensed and insured.",
                reviews = listOf(
                    ReviewUiState("1", "Ahmed Mohamed", 5, "Excellent service and very fast. He solved the problem in record time.", "Today"),
                    ReviewUiState("2", "Sara Ahmed", 4, "Good service, arrived on time and cleaned up after work.", "Yesterday")
                ),
                profilePhotoUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRsynS_f9S3stJ6S6T0V8LUM5VvOa3YqYy1Ag&s",
                isOnline = true
            )
        }
    }

    private fun getTechnicianProfile(id: String) {
        tryToExecute(
            onStart = { updateState { it.copy(isLoading = true) } },
            execute = { technicianRepository.getTechnicianDetails(id) },
            onSuccess = ::onGetProfileSuccess,
            onError = ::onGetProfileError
        )
    }

    private fun onGetProfileSuccess(technician: Technician) {
        updateState {
            it.copy(
                isLoading = false,
                name = "${technician.user.firstName} ${technician.user.lastName}",
                occupation = technician.serviceCategory.name,
                location = technician.user.address.fullAddress,
                rating = technician.averageRating.toString(),
                reviewCount = technician.reviewCount.toString(),
                experience = "${technician.yearsOfExperience} Years",
                bio = technician.bio,
                reviews = technician.reviews.toUiState(),
                profilePhotoUrl = technician.user.profilePhotoUrl,
                isOnline = technician.isAvailable
            )
        }
    }

    private fun onGetProfileError(throwable: Throwable) {
        updateState { it.copy(isLoading = false, error = throwable.message) }
        emitEffect(TechnicianProfileUiEffect.ShowSnackBar(throwable.message ?: "Error", true))
    }

    override fun onSettingsClicked() {
        emitEffect(TechnicianProfileUiEffect.NavigateToSettings)
    }

    override fun onViewAllReviewsClicked() {
        emitEffect(TechnicianProfileUiEffect.NavigateToAllReviews)
    }
}

private fun List<Review>.toUiState(): List<ReviewUiState> {
    return map {
        ReviewUiState(
            id = it.id,
            reviewerName = it.reviewerName,
            rating = it.rating,
            comment = it.comment,
            date = it.createdAt.toString() // Format date properly in a real app
        )
    }
}
