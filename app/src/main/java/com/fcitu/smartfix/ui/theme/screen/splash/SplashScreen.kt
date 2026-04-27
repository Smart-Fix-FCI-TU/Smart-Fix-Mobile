package com.fcitu.smartfix.ui.theme.screen.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.fcitu.smartfix.domain.model.UserRole
import com.fcitu.smartfix.domain.repository.IdentityRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import org.koin.compose.koinInject

// TODO: this is a temp splash screen, replace it with a real one later.
//  is used to check if the user is logged in and navigate to the appropriate screen
@Composable
fun SplashScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToCustomerHome: () -> Unit,
    onNavigateToTechnicianHome: () -> Unit,
    identityRepository: IdentityRepository = koinInject()
) {
    LaunchedEffect(Unit) {
        delay(1500)
        val isLoggedIn = identityRepository.getIsLoggedIn().first()
        if (isLoggedIn) {
            val role = identityRepository.getUserRole().first()
            if (role == UserRole.CUSTOMER) {
                onNavigateToCustomerHome()
            } else {
                onNavigateToTechnicianHome()
            }
        } else {
            onNavigateToLogin()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("SmartFix")
    }
}