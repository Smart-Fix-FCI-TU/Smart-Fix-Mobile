package com.fcitu.smartfix.ui.screen.technician.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
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
import com.fcitu.smartfix.ui.navigation.LocalNavController
import com.fcitu.smartfix.ui.navigation.Route
import com.fcitu.smartfix.ui.screen.technician.profile.component.TechnicianProfileContent
import com.fcitu.smartfix.ui.utils.EffectHandler
import kotlinx.coroutines.flow.SharedFlow
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TechnicianProfileScreen(
    viewModel: TechnicianProfileViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect = viewModel.effect

    EffectsHandler(
        effects = effect
    )

    Scaffold(
        topBar = {
            TechnicianProfileAppBar(
                onSettingsClick = viewModel::onSettingsClicked
            )
        }
    ) {
        TechnicianProfileContent(
            state = state,
            onViewAllReviewsClicked = viewModel::onViewAllReviewsClicked,
            modifier = Modifier.fillMaxSize()
        )
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
) {
    val snackBarHostController = LocalSnackBarHostController.current
    val navController = LocalNavController.current

    EffectHandler(effects = effects) { effect ->
        when (effect) {
            TechnicianProfileUiEffect.NavigateToSettings -> {
                navController.navigate(Route.Settings)
            }

            TechnicianProfileUiEffect.NavigateToAllReviews -> {
                navController.navigate(Route.AllReviews)
            }

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
