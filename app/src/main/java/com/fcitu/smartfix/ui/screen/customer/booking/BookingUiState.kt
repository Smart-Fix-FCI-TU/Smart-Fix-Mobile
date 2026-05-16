package com.fcitu.smartfix.ui.screen.customer.booking

import android.net.Uri

data class BookingUiState(
    val shortTitle: String = "",
    val detailedDescription: String = "",
    val problemPhotos: List<Uri> = emptyList(),
    val location: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val floor: String = "",
    val apartmentNo: String = "",
    val additionalNotes: String = "",
    val isShortTitleValid: Boolean = false,
    val isDetailedDescriptionValid: Boolean = false,
    val isLocationValid: Boolean = false,
    val isFindServiceButtonEnabled: Boolean = false,
    val showOrderDetailsBottomSheet: Boolean = false,
    val showDeletePhotoDialog: Boolean = false,
    val photoToDelete: Uri? = null,
    val showLocationDialog: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val isFormValid: Boolean
        get() = isShortTitleValid && isDetailedDescriptionValid && isLocationValid
}