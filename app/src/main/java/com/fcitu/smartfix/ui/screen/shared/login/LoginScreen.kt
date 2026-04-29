package com.fcitu.smartfix.ui.screen.shared.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fcitu.smartfix.domain.model.UserRole
import com.fcitu.smartfix.ui.designSystem.components.button.PrimaryButton
import com.fcitu.smartfix.ui.designSystem.components.snackBar.LocalSnackBarHostController
import com.fcitu.smartfix.ui.designSystem.components.snackBar.SnackBarData
import com.fcitu.smartfix.ui.utils.EffectHandler
import kotlinx.coroutines.flow.SharedFlow
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: (UserRole) -> Unit,
    viewModel: com.fcitu.smartfix.ui.screen.shared.login.LoginScreenViewModel = koinViewModel<com.fcitu.smartfix.ui.screen.shared.login.LoginScreenViewModel>()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effects = viewModel.effect

    _root_ide_package_.com.fcitu.smartfix.ui.screen.shared.login.EffectsHandler(
        effects = effects,
        onLoginSuccess = onLoginSuccess
    )

    _root_ide_package_.com.fcitu.smartfix.ui.screen.shared.login.LoginScreenContent(
        uiState = state,
        interactionListener = viewModel,
    )
}

@Composable
private fun LoginScreenContent(
    uiState: com.fcitu.smartfix.ui.screen.shared.login.LoginScreenUiState,
    interactionListener: com.fcitu.smartfix.ui.screen.shared.login.LoginScreenInteractionListener,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        _root_ide_package_.com.fcitu.smartfix.ui.screen.shared.login.component.WelcomeMessage(
            modifier = Modifier.padding(bottom = 24.dp)
        )

        _root_ide_package_.com.fcitu.smartfix.ui.screen.shared.login.component.RoleSelector(
            selectedRole = uiState.userRole,
            onRoleSelected = { interactionListener.onUserRoleSelected(it) },
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // TODO : Show error message if phone number is invalid
        _root_ide_package_.com.fcitu.smartfix.ui.screen.shared.login.component.LabeledInputPhoneNumber(
            value = uiState.phoneNumber,
            onValueChange = { interactionListener.onPhoneNumberChanged(it) },
            label = "Phone Number",
        )

        // TODO : Show error message if password is invalid
        _root_ide_package_.com.fcitu.smartfix.ui.screen.shared.login.component.LabeledInputPassword(
            value = uiState.password,
            onValueChange = { interactionListener.onPasswordChanged(it) },
            label = "Password",
            onTogglePasswordVisibility = { interactionListener.onPasswordVisibilityToggled() },
            isPasswordVisible = uiState.isPasswordVisible,
            onClickForgetPassword = { /* TODO : Not implemented yet */ },
            modifier = Modifier.padding(top = 16.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        PrimaryButton(
            text = "Login",
            onClick = interactionListener::onLoginClicked,
            isEnabled = uiState.isLoginEnabled && !uiState.isLoading,
            isLoading = uiState.isLoading,
            contentPadding = PaddingValues(vertical = 13.dp),
            containerColor = Color(0xFFFF4400),
            disabledContainerColor = Color(0xFFD9D9D9),
            contentColor = Color(0xFFFFFFFF),
            disabledContentColor = Color(0xFF000000),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        )

        _root_ide_package_.com.fcitu.smartfix.ui.screen.shared.login.component.AuthPrompt(
            message = "You are new?",
            actionLabel = "Register Now",
            onActionClick = { /* TODO : Not implemented yet */ },
            isEnabled = !uiState.isLoading
        )
    }
}

@Composable
private fun EffectsHandler(
    effects: SharedFlow<com.fcitu.smartfix.ui.screen.shared.login.LoginScreenUiEffect>,
    onLoginSuccess: (UserRole) -> Unit
) {
    val snackBarHostController = LocalSnackBarHostController.current

    EffectHandler(effects = effects) { effect ->
        when (effect) {
            is LoginScreenUiEffect.NavigateToHome -> {
                onLoginSuccess(effect.role)
            }

            is LoginScreenUiEffect.ShowSnackBar -> {
                snackBarHostController.showSnackBar(
                    SnackBarData(
                        title = if (effect.isError) "Error" else "Success",
                        message = effect.message,
                        isError = effect.isError
                    )
                )
            }
        }
    }
}