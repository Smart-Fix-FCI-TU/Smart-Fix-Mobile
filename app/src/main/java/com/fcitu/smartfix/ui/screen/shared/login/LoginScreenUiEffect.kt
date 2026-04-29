package com.fcitu.smartfix.ui.screen.shared.login

import com.fcitu.smartfix.domain.model.UserRole

sealed interface LoginScreenUiEffect {
    data class NavigateToHome(val role: UserRole) : LoginScreenUiEffect
    data class ShowSnackBar(val message: String, val isError: Boolean) : LoginScreenUiEffect
}