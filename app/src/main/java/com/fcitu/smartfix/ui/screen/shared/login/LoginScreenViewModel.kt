package com.fcitu.smartfix.ui.screen.shared.login

import com.fcitu.smartfix.domain.entity.User
import com.fcitu.smartfix.domain.exception.BadRequestException
import com.fcitu.smartfix.domain.exception.InvalidEmailException
import com.fcitu.smartfix.domain.exception.InvalidPasswordException
import com.fcitu.smartfix.domain.exception.NoNetworkException
import com.fcitu.smartfix.domain.exception.NotFoundException
import com.fcitu.smartfix.domain.exception.UnauthorizedException
import com.fcitu.smartfix.domain.exception.UserNotRegisteredException
import com.fcitu.smartfix.domain.useCase.LoginUseCase
import com.fcitu.smartfix.ui.shared.BaseViewModel
import kotlinx.coroutines.delay

class LoginScreenViewModel(
    private val loginUseCase: LoginUseCase,
) : BaseViewModel<LoginScreenUiState, LoginScreenUiEffect>(
    LoginScreenUiState()
), LoginScreenInteractionListener {

    override fun onEmailChanged(email: String) {
        updateState { it.copy(email = email) }
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
            val emailValid = loginUseCase.isEmailValid(email = it.email)
            val passwordValid = loginUseCase.isPasswordValid(password = it.password)
            it.copy(isLoginEnabled = passwordValid && emailValid)
        }
    }

    private fun onLoginStart() {
        updateState { it.copy(isLoading = true) }
    }

    private suspend fun onLogin() = loginUseCase.login(
        email = state.value.email,
        password = state.value.password
    )

    private suspend fun onLoginSuccess(user: User) {
        updateState { it.copy(isLoading = false) }
        emitEffect(LoginScreenUiEffect.ShowSnackBar("Login successful", isError = false))
        delay(100)
        emitEffect(LoginScreenUiEffect.NavigateToHome(user.role))
    }

    private fun onLoginError(throwable: Throwable) {
        updateState { it.copy(isLoading = false) }
        mapError(throwable)
    }

    private fun mapError(throwable: Throwable) {
        when (throwable) {
            is InvalidEmailException -> {
                emitEffect(
                    LoginScreenUiEffect.ShowSnackBar(
                        message = "Invalid email format",
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
                        message = "Email not registered",
                        isError = true
                    )
                )
            }

            is UnauthorizedException -> {
                emitEffect(
                    LoginScreenUiEffect.ShowSnackBar(
                        message = "Incorrect email or password",
                        isError = true
                    )
                )
            }

            is NotFoundException -> {
                emitEffect(
                    LoginScreenUiEffect.ShowSnackBar(
                        message = "No account found with this email",
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