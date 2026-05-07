package com.fcitu.smartfix.ui.screen.shared.login

import com.fcitu.smartfix.domain.model.UserRole

interface LoginScreenInteractionListener {
    fun onEmailChanged(email: String)
    fun onPasswordChanged(password: String)
    fun onPasswordVisibilityToggled()
    fun onLoginClicked()
}