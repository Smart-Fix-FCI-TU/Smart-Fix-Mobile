package com.fcitu.smartfix.ui.theme.screen.describeProblem

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.fcitu.smartfix.domain.useCase.BookingUseCase
import com.fcitu.smartfix.ui.theme.shared.BaseViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class BookingViewModel(
    private val bookingUseCase: BookingUseCase
) : BaseViewModel<BookingUiState, BookingUiEffect>(BookingUiState()), BookingInteractionListener {

    private val _uiState = MutableStateFlow(BookingUiState())
    val uiState: StateFlow<BookingUiState> = _uiState.asStateFlow()

    private var serviceId: String = ""

    fun setServiceId(id: String) {
        serviceId = id
    }

    private val _uiEffect = Channel<BookingUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    private fun updateState(newState: BookingUiState) {
        _uiState.value = newState
    }

    private fun sendEffect(effect: BookingUiEffect) {
        viewModelScope.launch {
            _uiEffect.send(effect)
        }
    }

    private fun validateForm() {
        val currentState = _uiState.value
        val isButtonEnabled = currentState.isShortTitleValid &&
                currentState.isDetailedDescriptionValid &&
                currentState.isLocationValid
        updateState(currentState.copy(isFindServiceButtonEnabled = isButtonEnabled))
    }

    override fun onShortTitleChanged(title: String) {
        updateState(
            _uiState.value.copy(
                shortTitle = title,
                isShortTitleValid = title.isNotBlank()
            )
        )
        validateForm()
    }

    override fun onDetailedDescriptionChanged(description: String) {
        if (description.length > 500) return
        updateState(
            _uiState.value.copy(
                detailedDescription = description,
                isDetailedDescriptionValid = description.isNotBlank()
            )
        )
        validateForm()
    }

    override fun onProblemPhotoAdded(uris: List<Uri>) {
        val currentPhotos = _uiState.value.problemPhotos.toMutableList()
        uris.forEach { uri ->
            if (currentPhotos.size < 5 && !currentPhotos.contains(uri)) {
                currentPhotos.add(uri)
            }
        }
        updateState(_uiState.value.copy(problemPhotos = currentPhotos))
    }

    override fun onProblemPhotoRemoved(uri: Uri) {
        val currentPhotos = _uiState.value.problemPhotos.toMutableList()
        currentPhotos.remove(uri)
        updateState(_uiState.value.copy(problemPhotos = currentPhotos))
    }

    override fun onAddPhotoClicked() {
        sendEffect(BookingUiEffect.LaunchImagePicker(5 - _uiState.value.problemPhotos.size))
    }

    override fun onLocationClicked() {
        sendEffect(BookingUiEffect.NavigateToMap)
    }

    override fun onFloorChanged(floor: String) {
        updateState(_uiState.value.copy(floor = floor))
    }

    override fun onApartmentNoChanged(apartmentNo: String) {
        updateState(_uiState.value.copy(apartmentNo = apartmentNo))
    }

    override fun onAdditionalNotesChanged(notes: String) {
        updateState(_uiState.value.copy(additionalNotes = notes))
    }

    override fun onFindServiceClicked() {
        val currentState = _uiState.value
        if (currentState.isFormValid) {
            updateState(currentState.copy(showOrderDetailsBottomSheet = true))
        } else {
            sendEffect(BookingUiEffect.ShowSnackBar(
                "Please fill in all required fields.",
                isError = true
            ))
        }
    }

    override fun onMapPlaceholderClicked() {
        sendEffect(BookingUiEffect.NavigateToMap)
    }

    override fun onHomeChipClicked() {
        updateState(
            _uiState.value.copy(
                location = "Tahrir St, Maadi, Cairo",
                isLocationValid = true
            )
        )
        validateForm()
    }

    override fun onWorkChipClicked() {
        updateState(
            _uiState.value.copy(
                location = "123 Work Ave, Giza",
                isLocationValid = true
            )
        )
        validateForm()
    }

    override fun onBottomSheetDismissed() {
        updateState(_uiState.value.copy(showOrderDetailsBottomSheet = false))
    }

    override fun onFindAvailableTechnicianClicked() {
        viewModelScope.launch {
            updateState(_uiState.value.copy(isLoading = true))
            val currentState = _uiState.value
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
                updateState(
                    _uiState.value.copy(
                        isLoading = false,
                        showOrderDetailsBottomSheet = false
                    )
                )
                sendEffect(BookingUiEffect.ProblemSubmittedSuccessfully)
            }.onFailure {
                updateState(_uiState.value.copy(isLoading = false, errorMessage = it.message))
                sendEffect(
                    BookingUiEffect.ShowSnackBar(
                        it.message ?: "Unknown error occurred",
                        isError = true
                    )
                )
            }
        }
    }

    override fun onLocationSelected(location: String, latitude: Double?, longitude: Double?) {
        updateState(
            _uiState.value.copy(
                location = location,
                latitude = latitude,
                longitude = longitude,
                isLocationValid = location.isNotBlank()
            )
        )
        validateForm()
    }
}