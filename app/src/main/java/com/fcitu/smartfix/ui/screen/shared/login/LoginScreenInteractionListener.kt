package com.fcitu.smartfix.ui.screen.shared.login

interface LoginScreenInteractionListener {
    fun onEmailChanged(email: String)
    fun onPasswordChanged(password: String)
    fun onPasswordVisibilityToggled()
    fun onLoginClicked()
}