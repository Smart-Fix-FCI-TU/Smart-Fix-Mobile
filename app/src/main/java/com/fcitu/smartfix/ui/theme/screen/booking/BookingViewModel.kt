package com.fcitu.smartfix.ui.theme.screen.booking

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.fcitu.smartfix.domain.useCase.BookingUseCase
import com.fcitu.smartfix.ui.theme.shared.BaseViewModel
import kotlinx.coroutines.launch

class BookingViewModel(
    private val bookingUseCase: BookingUseCase
) : BaseViewModel<BookingUiState, BookingUiEffect>(BookingUiState()), BookingInteractionListener {

    private var serviceId: String = ""

    fun setServiceId(id: String) {
        serviceId = id
    }

    private fun validateForm() {
        val currentState = state.value
        val isButtonEnabled = currentState.isShortTitleValid &&
                currentState.isDetailedDescriptionValid &&
                currentState.isLocationValid
        updateState { it.copy(isFindServiceButtonEnabled = isButtonEnabled) }
    }

    override fun onShortTitleChanged(title: String) {
        updateState {
            it.copy(
                shortTitle = title,
                isShortTitleValid = title.isNotBlank()
            )
        }
        validateForm()
    }

    override fun onDetailedDescriptionChanged(description: String) {
        if (description.length > 500) return
        updateState {
            it.copy(
                detailedDescription = description,
                isDetailedDescriptionValid = description.isNotBlank()
            )
        }
        validateForm()
    }

    override fun onProblemPhotoAdded(uris: List<Uri>) {
        val currentPhotos = state.value.problemPhotos.toMutableList()
        uris.forEach { uri ->
            if (currentPhotos.size < 5 && !currentPhotos.contains(uri)) {
                currentPhotos.add(uri)
            }
        }
        updateState { it.copy(problemPhotos = currentPhotos) }
    }

    override fun onProblemPhotoRemoved(uri: Uri) {
        val currentPhotos = state.value.problemPhotos.toMutableList()
        currentPhotos.remove(uri)
        updateState { it.copy(problemPhotos = currentPhotos) }
    }

    override fun onAddPhotoClicked() {
        emitEffect(BookingUiEffect.LaunchImagePicker(5 - state.value.problemPhotos.size))
    }

    override fun onLocationClicked() {
        emitEffect(BookingUiEffect.NavigateToMap)
    }

    override fun onFloorChanged(floor: String) {
        updateState { it.copy(floor = floor) }
    }

    override fun onApartmentNoChanged(apartmentNo: String) {
        updateState { it.copy(apartmentNo = apartmentNo) }
    }

    override fun onAdditionalNotesChanged(notes: String) {
        updateState { it.copy(additionalNotes = notes) }
    }

    override fun onFindServiceClicked() {
        val currentState = state.value
        if (currentState.isFormValid) {
            updateState { it.copy(showOrderDetailsBottomSheet = true) }
        } else {
            emitEffect(
                BookingUiEffect.ShowSnackBar(
                    "Please fill in all required fields.",
                    isError = true
                )
            )
        }
    }

    override fun onMapPlaceholderClicked() {
        emitEffect(BookingUiEffect.NavigateToMap)
    }

    override fun onBottomSheetDismissed() {
        updateState { it.copy(showOrderDetailsBottomSheet = false) }
    }

    override fun onFindAvailableTechnicianClicked() {
        viewModelScope.launch {
            updateState { it.copy(isLoading = true) }
            val currentState = state.value
            val result = bookingUseCase(
                serviceId = serviceId,
                shortTitle = currentState.shortTitle,
                detailedDescription = currentState.detailedDescription,
                problemPhotos = currentState.problemPhotos,
                location = currentState.location,
                floor = currentState.floor.ifBlank { null },
                apartmentNo = currentState.apartmentNo.ifBlank { null },
                additionalNotes = currentState.additionalNotes.ifBlank { null }
            )
            result.onSuccess {
                updateState {
                    it.copy(
                        isLoading = false,
                        showOrderDetailsBottomSheet = false
                    )
                }
                emitEffect(BookingUiEffect.ProblemSubmittedSuccessfully)
            }.onFailure { error ->
                updateState { it.copy(isLoading = false, errorMessage = error.message) }
                emitEffect(
                    BookingUiEffect.ShowSnackBar(
                        error.message ?: "Unknown error occurred",
                        isError = true
                    )
                )
            }
        }
    }

    override fun onLocationSelected(location: String, latitude: Double?, longitude: Double?) {
        updateState {
            it.copy(
                location = location,
                latitude = latitude,
                longitude = longitude,
                isLocationValid = location.isNotBlank()
            )
        }
        validateForm()
    }
}