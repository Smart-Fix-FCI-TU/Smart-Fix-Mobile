package com.fcitu.smartfix.ui.theme.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToCustomerHome: () -> Unit,
    onNavigateToTechnicianHome: () -> Unit,
) {
    LaunchedEffect(Unit) {
        delay(1500)
        // TODO: check session from DataStore and route accordingly
        // For now always go to Login
        onNavigateToLogin()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // TODO: replace with real branding
        Text("SmartFix")
    }
}