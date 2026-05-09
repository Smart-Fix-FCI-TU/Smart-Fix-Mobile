package com.fcitu.smartfix.ui.screen.shared.login

data class LoginScreenUiState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val isLoginEnabled: Boolean = false,
)
