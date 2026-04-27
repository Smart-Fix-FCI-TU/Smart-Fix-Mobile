package com.fcitu.smartfix.ui.theme.screen.login

import com.fcitu.smartfix.domain.model.UserRole

data class LoginScreenUiState(
    val userRole: UserRole = UserRole.CUSTOMER,
    val phoneNumber: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val isLoginEnabled: Boolean = false,
)
// TODO : Add error message for phone number and password