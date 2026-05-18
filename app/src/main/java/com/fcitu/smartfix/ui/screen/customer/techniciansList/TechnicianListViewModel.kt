package com.fcitu.smartfix.ui.screen.customer.techniciansList

import androidx.lifecycle.viewModelScope
import com.fcitu.smartfix.domain.entity.Address
import com.fcitu.smartfix.domain.entity.Review
import com.fcitu.smartfix.domain.entity.Technician
import com.fcitu.smartfix.domain.entity.User
import com.fcitu.smartfix.domain.model.ServiceCategory
import com.fcitu.smartfix.domain.model.UserRole
import com.fcitu.smartfix.ui.shared.BaseViewModel
import com.fcitu.smartfix.ui.utils.now
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import java.util.Locale

class TechnicianListViewModel :
    BaseViewModel<TechnicianListUiState, TechnicianListEffect>(TechnicianListUiState()),
    TechnicianListInteractionListener {

    private var timerJob: Job? = null

    init {
        getTechnicians()
    }

    private fun getTechnicians() {
        val fakeTechnicians = createFakeTechnicians()
        updateState { it.copy(technicians = fakeTechnicians) }
    }

    private fun createFakeTechnicians(): List<Technician> {
        return List(6) { index ->
            Technician(
                user = User(
                    id = index.toString(),
                    phoneNumber = "050000000$index",
                    firstName = "Technician",
                    lastName = "#$index",
                    username = "tech_$index",
                    birthOfDate = "1990-01-01",
                    nationalId = "123456789$index",
                    email = "tech$index@example.com",
                    role = UserRole.TECHNICIAN,
                    profilePhotoUrl = "https://ui-avatars.com/api/?name=Tech+$index",
                    address = Address(
                        id = index.toString(),
                        fullAddress = "123 Main St, City $index",
                        location = Address.Location(
                            latitude = 37.7749 + index * 0.01,
                            longitude = -122.4194 + index * 0.01
                        ),
                        floor = "4",
                        apartmentNo = "5"
                    )
                ),
                serviceCategory = when (index) {
                    0 -> ServiceCategory.PLUMBING
                    1 -> ServiceCategory.ELECTRICITY
                    2 -> ServiceCategory.CONDITIONING
                    3 -> ServiceCategory.PAINTING
                    5 -> ServiceCategory.CARPENTRY
                    4 -> ServiceCategory.PAINTING
                    else -> ServiceCategory.PLUMBING
                },
                isAvailable = true,
                isOnJob = false,
                yearsOfExperience = (2..10).random(),
                bio = "Experienced technician providing high-quality repair services with focus on reliability.",
                averageRating = (3..5).random().toFloat(),
                reviewCount = (10..50).random(),
                reviews = List(3) { index ->
                    Review(
                        id = index.toString(),
                        reviewerName = if (index % 2 == 0) "Ahmed Mohamed" else "Sara Ahmed",
                        rating = if (index % 3 == 0) 4 else 5,
                        comment = "Service number $index: Very professional and efficient. Highly recommended!",
                        createdAt = LocalDateTime.now()
                    )
                }
            )
        }
    }

    override fun onClickFilter(filter: FilterType) {
        updateState { it.copy(selectedFilter = filter) }
        // In a real app, we would filter the list here
        when (filter) {
            FilterType.TOP_RATED -> {
                val sortedTechnicians =
                    state.value.technicians.sortedByDescending { it.averageRating }
                updateState { it.copy(technicians = sortedTechnicians) }
            }

            FilterType.NEAREST -> {
                // For simplicity, we won't implement actual location-based sorting here
                val shuffledTechnicians = state.value.technicians.shuffled()
                updateState { it.copy(technicians = shuffledTechnicians) }
            }

            else -> {
                getTechnicians() // Reset to original list
            }
        }
    }

    override fun onClickTechnician(technician: Technician) {
        updateState {
            it.copy(
                selectedTechnician = technician,
                isTechnicianProfileSheetVisible = true
            )
        }
    }

    override fun onClickOrderNow(technician: Technician) {
        updateState {
            it.copy(
                selectedTechnician = technician,
                isWaitingTechnicianSheetVisible = true,
                isTechnicianProfileSheetVisible = false
            )
        }
        startTimer()
    }

    override fun onDismissProfileSheet() {
        updateState { it.copy(isTechnicianProfileSheetVisible = false) }
    }

    override fun onDismissWaitingSheet() {
        updateState { it.copy(isWaitingTechnicianSheetVisible = false) }
    }

    override fun onDismissAcceptedSheet() {
        updateState { it.copy(isAcceptedSheetVisible = false) }
        stopTimer()
    }

    override fun onDismissRejectedSheet() {
        updateState { it.copy(isRejectedSheetVisible = false) }
    }

    override fun onClickCancelOrder() {
        onDismissWaitingSheet()
        updateState { it.copy(isRejectedSheetVisible = true) }
        stopTimer()
    }

    override fun onClickBack() {
        emitEffect(TechnicianListEffect.NavigateBack)
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            var seconds = 120
            while (seconds > 0) {
                delay(1000)
                seconds--
                val minutes = seconds / 60
                val remainingSeconds = seconds % 60
                val timeString =
                    String.format(Locale.getDefault(), "%02d:%02d", minutes, remainingSeconds)
                updateState { it.copy(countdownTime = timeString) }

                // Simulate acceptance after 5 seconds for testing
                if (seconds == 115) {
                    onDismissWaitingSheet()
                    updateState { it.copy(isAcceptedSheetVisible = true) }
                    break
                }
            }
            if (seconds == 0) {
                onDismissWaitingSheet()
                updateState { it.copy(isRejectedSheetVisible = true) }
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        updateState { it.copy(countdownTime = "02:00") }
    }
}