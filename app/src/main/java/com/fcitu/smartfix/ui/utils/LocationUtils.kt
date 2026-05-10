package com.fcitu.smartfix.ui.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import java.util.Locale

object LocationUtils {

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(
        context: Context,
        onResult: (address: String, lat: Double, lng: Double) -> Unit,
        onError: (Exception) -> Unit = {}
    ) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        val geocoder = Geocoder(context, Locale.getDefault())
        val cancellationTokenSource = CancellationTokenSource()

        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            cancellationTokenSource.token
        ).addOnSuccessListener { location ->
            if (location != null) {
                try {
                    @Suppress("DEPRECATION")
                    val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    val addressLine = addresses?.firstOrNull()?.getAddressLine(0) ?: "Location Found"
                    onResult(addressLine, location.latitude, location.longitude)
                } catch (e: Exception) {
                    onResult(
                        "Location Found (${location.latitude}, ${location.longitude})",
                        location.latitude,
                        location.longitude
                    )
                }
            } else {
                onError(Exception("Location is null"))
            }
        }.addOnFailureListener {
            onError(it)
        }
    }
}
