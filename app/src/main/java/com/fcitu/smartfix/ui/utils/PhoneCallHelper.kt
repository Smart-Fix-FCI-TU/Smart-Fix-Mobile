package com.fcitu.smartfix.ui.utils

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

fun Context.makePhoneCall(phoneNumber: String) {
    val intent = Intent(Intent.ACTION_DIAL).apply {
        data = "tel:$phoneNumber".toUri()
    }
    startActivity(intent)
}