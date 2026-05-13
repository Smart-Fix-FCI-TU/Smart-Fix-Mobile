package com.fcitu.smartfix.ui.screen.technician.profile

import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fcitu.smartfix.ui.designSystem.components.appBar.AppBar
import com.fcitu.smartfix.ui.designSystem.components.scaffold.Scaffold
import com.fcitu.smartfix.ui.designSystem.components.snackBar.LocalSnackBarHostController
import com.fcitu.smartfix.ui.designSystem.components.snackBar.SnackBarData
import com.fcitu.smartfix.ui.screen.technician.profile.component.TechnicianProfileContent
import com.fcitu.smartfix.ui.utils.EffectHandler
import kotlinx.coroutines.flow.SharedFlow
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TechnicianProfileScreen(
    onBack: () -> Unit,
    onSettings: () -> Unit,
    onViewAllReviews: () -> Unit,
    viewModel: TechnicianProfileViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect = viewModel.effect

    EffectsHandler(
        effects = effect,
        onBack = onBack,
        onSettings = onSettings,
        onViewAllReviews = onViewAllReviews
    )

    Scaffold(
        topBar = {
            TechnicianProfileAppBar(
                onSettingsClick = viewModel::onSettingsClicked
            )
        }
    ) {
        TechnicianProfileContent(state, viewModel)
    }
}

@Composable
private fun TechnicianProfileAppBar(
    onSettingsClick: () -> Unit,
) {
    AppBar(
        title = "My Profile",
        trailingContent = {
            Icon(
                painter = painterResource(com.fcitu.smartfix.R.drawable.ic_settings),
                contentDescription = "Settings",
                modifier = Modifier.clickable { onSettingsClick() }
            )
        }
    )
}

@Composable
private fun EffectsHandler(
    effects: SharedFlow<TechnicianProfileUiEffect>,
    onBack: () -> Unit,
    onSettings: () -> Unit,
    onViewAllReviews: () -> Unit,
) {
    val snackBarHostController = LocalSnackBarHostController.current

    EffectHandler(effects = effects) { effect ->
        when (effect) {
            TechnicianProfileUiEffect.NavigateBack -> onBack()
            TechnicianProfileUiEffect.NavigateToSettings -> onSettings()
            TechnicianProfileUiEffect.NavigateToAllReviews -> onViewAllReviews()
            is TechnicianProfileUiEffect.ShowSnackBar -> {
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
