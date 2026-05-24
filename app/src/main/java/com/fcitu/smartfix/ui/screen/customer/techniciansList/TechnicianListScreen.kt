package com.fcitu.smartfix.ui.screen.customer.techniciansList

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.fcitu.smartfix.ui.screen.customer.techniciansList.component.TechnicianListContent
import com.fcitu.smartfix.ui.utils.EffectHandler
import kotlinx.coroutines.flow.SharedFlow
import org.koin.androidx.compose.koinViewModel

@Composable
fun TechnicianListScreen(
    onBackClicked: () -> Unit,
    viewModel: TechnicianListViewModel = koinViewModel()
) {
    BackHandler(enabled = true) {
        viewModel.onClickBack()
    }

    val state by viewModel.state.collectAsState()
    val effects = viewModel.effect


    EffectsHandler(
        effects,
        onBackClicked = onBackClicked
    )

    TechnicianListContent(
        state = state,
        listener = viewModel
    )
}

@Composable
private fun EffectsHandler(
    effects: SharedFlow<TechnicianListEffect>,
    onBackClicked: () -> Unit
) {
    EffectHandler(
        effects = effects
    ) { effect ->
        when (effect) {
            is TechnicianListEffect.NavigateBack -> {
                onBackClicked()
            }
        }
    }
}