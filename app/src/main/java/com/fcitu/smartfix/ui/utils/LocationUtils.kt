package com.fcitu.smartfix.ui.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import java.util.Locale
import kotlin.concurrent.thread

object LocationUtils {

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(
        context: Context,
        onResult: (address: String, lat: Double, lng: Double) -> Unit,
        onError: (Exception) -> Unit = {}
    ) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        val cancellationTokenSource = CancellationTokenSource()

        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            cancellationTokenSource.token
        ).addOnSuccessListener { location ->
            if (location != null) {
                fetchAddress(context, location.latitude, location.longitude, onResult)
            } else {
                fusedLocationClient.lastLocation.addOnSuccessListener { lastLocation ->
                    if (lastLocation != null) {
                        fetchAddress(context, lastLocation.latitude, lastLocation.longitude, onResult)
                    } else {
                        onError(Exception("Unable to find location. Please ensure GPS is on."))
                    }
                }
            }
        }.addOnFailureListener { onError(it) }
    }

    private fun fetchAddress(
        context: Context,
        lat: Double,
        lng: Double,
        onResult: (String, Double, Double) -> Unit
    ) {
        thread {
            try {
                val geocoder = Geocoder(context, Locale.getDefault())
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocation(lat, lng, 1)
                val addressLine = addresses?.firstOrNull()?.getAddressLine(0) ?: "Selected Location ($lat, $lng)"
                
                android.os.Handler(android.os.Looper.getMainLooper()).post {
                    onResult(addressLine, lat, lng)
                }
            } catch (e: Exception) {
                android.os.Handler(android.os.Looper.getMainLooper()).post {
                    onResult("Location: $lat, $lng", lat, lng)
                }
            }
        }
    }
}
