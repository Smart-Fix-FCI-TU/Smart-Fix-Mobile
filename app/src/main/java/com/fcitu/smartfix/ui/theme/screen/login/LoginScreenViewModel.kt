package com.fcitu.smartfix.ui.theme.screen.login

import com.fcitu.smartfix.domain.entity.User
import com.fcitu.smartfix.domain.exception.InvalidMobileNumberException
import com.fcitu.smartfix.domain.exception.InvalidPasswordException
import com.fcitu.smartfix.domain.exception.NoNetworkException
import com.fcitu.smartfix.domain.exception.UserNotRegisteredException
import com.fcitu.smartfix.domain.model.UserRole
import com.fcitu.smartfix.domain.repository.IdentityRepository
import com.fcitu.smartfix.domain.useCase.LoginUseCase
import com.fcitu.smartfix.ui.theme.shared.BaseViewModel

class LoginScreenViewModel(
    private val loginUseCase: LoginUseCase,
    private val identityRepository: IdentityRepository,
) : BaseViewModel<LoginScreenUiState, LoginScreenUiEffect>(
    LoginScreenUiState()
), LoginScreenInteractionListener {

    override fun onUserRoleSelected(userRole: UserRole) {
        updateState { it.copy(userRole = userRole) }
    }

    override fun onPhoneNumberChanged(phone: String) {
        updateState { it.copy(phoneNumber = phone) }
        changeIsLoginEnabled()
    }

    override fun onPasswordChanged(password: String) {
        updateState { it.copy(password = password) }
        changeIsLoginEnabled()
    }

    override fun onPasswordVisibilityToggled() {
        updateState { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    override fun onLoginClicked() {
        tryToExecute(
            onStart = ::onLoginStart,
            execute = ::onLogin,
            onSuccess = ::onLoginSuccess,
            onError = ::onLoginError
        )
    }

    private fun onLoginStart() {
        updateState { it.copy(isLoading = true) }
    }

    private suspend fun onLogin() = loginUseCase.login(
        role = state.value.userRole,
        phoneNumber = state.value.phoneNumber,
        password = state.value.password
    )

    private suspend fun onLoginSuccess(user: User) {
        identityRepository.saveSession(role = state.value.userRole, isLoggedIn = true)
        updateState { it.copy(isLoading = false) }
        emitEffect(LoginScreenUiEffect.ShowSnackBar("Login successful", isError = false))
        emitEffect(LoginScreenUiEffect.NavigateToHome(state.value.userRole))
    }

    private fun onLoginError(throwable: Throwable) {
        updateState { it.copy(isLoading = false) }
        when (throwable) {
            is InvalidMobileNumberException -> {
                emitEffect(
                    LoginScreenUiEffect.ShowSnackBar(
                        message = "Invalid phone number format",
                        isError = true
                    )
                )
            }

            is InvalidPasswordException -> {
                emitEffect(
                    LoginScreenUiEffect.ShowSnackBar(
                        message = "Invalid password format",
                        isError = true
                    )
                )
            }

            is UserNotRegisteredException -> {
                emitEffect(
                    LoginScreenUiEffect.ShowSnackBar(
                        message = "Phone number not registered",
                        isError = true
                    )
                )
            }

            is NoNetworkException -> {
                emitEffect(
                    LoginScreenUiEffect.ShowSnackBar(
                        message = "No Internet Connection",
                        isError = true
                    )
                )
            }

            else -> {
                println("onLoginError ${throwable.message}")
                emitEffect(
                    LoginScreenUiEffect.ShowSnackBar(
                        message = "An unknown error occurred",
                        isError = true
                    )
                )
            }
        }
    }

    private fun changeIsLoginEnabled() {
        updateState {
            val mobileNumberValid =
                loginUseCase.isMobileNumberValid(phoneNumber = state.value.phoneNumber)
            val passwordValid = loginUseCase.isPasswordValid(password = state.value.password)
            it.copy(isLoginEnabled = passwordValid && mobileNumberValid)
        }
    }
}