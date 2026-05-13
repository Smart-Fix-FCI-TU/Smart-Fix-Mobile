package com.fcitu.smartfix.ui.screen.customer.profile

import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fcitu.smartfix.R
import com.fcitu.smartfix.ui.designSystem.components.appBar.AppBar
import com.fcitu.smartfix.ui.designSystem.components.scaffold.Scaffold
import com.fcitu.smartfix.ui.designSystem.components.snackBar.LocalSnackBarHostController
import com.fcitu.smartfix.ui.designSystem.components.snackBar.SnackBarData
import com.fcitu.smartfix.ui.navigation.LocalNavController
import com.fcitu.smartfix.ui.navigation.Route
import com.fcitu.smartfix.ui.screen.customer.profile.component.CustomerProfileContent
import com.fcitu.smartfix.ui.utils.EffectHandler
import kotlinx.coroutines.flow.SharedFlow
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CustomerProfileScreen(
    viewModel: CustomerProfileViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect = viewModel.effect

    EffectsHandler(
        effects = effect
    )

    Scaffold(
        topBar = {
            CustomerProfileAppBar(
                onSettingsClick = viewModel::onSettingsClicked
            )
        }
    ) {
        CustomerProfileContent(state, viewModel)
    }
}

@Composable
private fun CustomerProfileAppBar(
    onSettingsClick: () -> Unit
) {
    AppBar(
        title = "My Profile",
        trailingContent = {
            Icon(
                painter = painterResource(R.drawable.ic_settings),
                contentDescription = "Settings",
                modifier = Modifier.clickable { onSettingsClick() }
            )
        }
    )
}

@Composable
private fun EffectsHandler(
    effects: SharedFlow<CustomerProfileUiEffect>
) {
    val snackBarHostController = LocalSnackBarHostController.current
    val navController = LocalNavController.current

    EffectHandler(effects = effects) { effect ->
        when (effect) {
            CustomerProfileUiEffect.NavigateToSettings -> {
                navController.navigate(Route.Settings)
            }
            CustomerProfileUiEffect.NavigateToAllServiceHistory -> {
                navController.navigate(Route.CustomerOrders)
            }
            is CustomerProfileUiEffect.ShowSnackBar -> {
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
