package com.fcitu.smartfix.ui.screen.shared.login

import com.fcitu.smartfix.domain.entity.User
import com.fcitu.smartfix.domain.exception.BadRequestException
import com.fcitu.smartfix.domain.exception.InvalidMobileNumberException
import com.fcitu.smartfix.domain.exception.InvalidPasswordException
import com.fcitu.smartfix.domain.exception.NoNetworkException
import com.fcitu.smartfix.domain.exception.NotFoundException
import com.fcitu.smartfix.domain.exception.UnauthorizedException
import com.fcitu.smartfix.domain.exception.UserNotRegisteredException
import com.fcitu.smartfix.domain.model.UserRole
import com.fcitu.smartfix.domain.useCase.LoginUseCase
import com.fcitu.smartfix.ui.shared.BaseViewModel
import kotlinx.coroutines.delay

class LoginScreenViewModel(
    private val loginUseCase: LoginUseCase,
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

    private fun changeIsLoginEnabled() {
        updateState {
            val mobileNumberValid =
                loginUseCase.isMobileNumberValid(phoneNumber = state.value.phoneNumber)
            val passwordValid = loginUseCase.isPasswordValid(password = state.value.password)
            it.copy(isLoginEnabled = passwordValid && mobileNumberValid)
        }
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
        updateState { it.copy(isLoading = false) }
        emitEffect(LoginScreenUiEffect.ShowSnackBar("Login successful", isError = false))
        delay(100)
        emitEffect(LoginScreenUiEffect.NavigateToHome(state.value.userRole))
    }

    private fun onLoginError(throwable: Throwable) {
        updateState { it.copy(isLoading = false) }
        mapError(throwable)
    }

    private fun mapError(throwable: Throwable) {
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

            is UnauthorizedException -> {
                emitEffect(
                    LoginScreenUiEffect.ShowSnackBar(
                        message = "Incorrect phone number or password",
                        isError = true
                    )
                )
            }

            is NotFoundException -> {
                emitEffect(
                    LoginScreenUiEffect.ShowSnackBar(
                        message = "No account found with this phone number",
                        isError = true
                    )
                )
            }

            is BadRequestException -> {
                emitEffect(
                    LoginScreenUiEffect.ShowSnackBar(
                        message = "Invalid request. Please check your inputs.",
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
                emitEffect(
                    LoginScreenUiEffect.ShowSnackBar(
                        message = "An unknown error occurred",
                        isError = true
                    )
                )
            }
        }
    }
}