package com.fcitu.smartfix.ui.theme.screen.login

import com.fcitu.smartfix.domain.model.UserRole

interface LoginScreenInteractionListener {
    fun onUserRoleSelected(userRole: UserRole)
    fun onPhoneNumberChanged(phone: String)
    fun onPasswordChanged(password: String)
    fun onPasswordVisibilityToggled()
    fun onLoginClicked()
}