package com.fcitu.smartfix.ui.screen.customer.booking

import android.net.Uri

interface BookingInteractionListener {
    fun onShortTitleChanged(title: String)
    fun onDetailedDescriptionChanged(description: String)
    fun onProblemPhotoAdded(uris: List<Uri>)
    fun onProblemPhotoRemoved(uri: Uri)
    fun onAddPhotoClicked()
    fun onLocationClicked()
    fun onFloorChanged(floor: String)
    fun onApartmentNoChanged(apartmentNo: String)
    fun onAdditionalNotesChanged(notes: String)
    fun onFindServiceClicked()
    fun onMapPlaceholderClicked()
    fun onBottomSheetDismissed()
    fun onFindAvailableTechnicianClicked()
    fun onLocationSelected(location: String, latitude: Double? = null, longitude: Double? = null)
}