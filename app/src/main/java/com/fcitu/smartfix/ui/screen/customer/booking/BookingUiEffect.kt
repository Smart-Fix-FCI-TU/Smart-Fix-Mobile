package com.fcitu.smartfix.ui.screen.customer.booking

import android.net.Uri

sealed interface BookingUiEffect {
    data object RequestLocation : BookingUiEffect
    data class ShowSnackBar(val message: String, val isError: Boolean) : BookingUiEffect
    data class LaunchImagePicker(val maxSelection: Int) : BookingUiEffect
    data class ShowImageRemoveConfirmation(val uri: Uri) : BookingUiEffect
    data object ProblemSubmittedSuccessfully : BookingUiEffect
}