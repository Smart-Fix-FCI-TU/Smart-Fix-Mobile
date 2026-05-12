package com.fcitu.smartfix.ui.screen.shared.orderDetails

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fcitu.smartfix.domain.model.UserRole
import com.fcitu.smartfix.ui.designSystem.components.bottomSheet.BottomSheet
import com.fcitu.smartfix.ui.designSystem.components.scaffold.Scaffold
import com.fcitu.smartfix.ui.designSystem.components.snackBar.LocalSnackBarHostController
import com.fcitu.smartfix.ui.designSystem.components.snackBar.SnackBarData
import com.fcitu.smartfix.ui.navigation.LocalNavController
import com.fcitu.smartfix.ui.navigation.Route
import com.fcitu.smartfix.ui.screen.shared.orderDetails.component.OrderDetailsFooter
import com.fcitu.smartfix.ui.screen.shared.orderDetails.component.OrderDetailsHeader
import com.fcitu.smartfix.ui.screen.shared.orderDetails.component.OrderInfo
import com.fcitu.smartfix.ui.screen.shared.orderDetails.component.OrderTimeline
import com.fcitu.smartfix.ui.screen.shared.orderDetails.component.PhotosSection
import com.fcitu.smartfix.ui.screen.shared.orderDetails.component.RatingBottomSheet
import com.fcitu.smartfix.ui.screen.shared.orderDetails.component.RatingInfo
import com.fcitu.smartfix.ui.screen.shared.orderDetails.component.SuccessBanner
import com.fcitu.smartfix.ui.utils.EffectHandler
import com.fcitu.smartfix.ui.utils.format
import kotlinx.coroutines.flow.SharedFlow
import org.koin.androidx.compose.koinViewModel

@Composable
fun OrderDetailsScreen(
    userRole: UserRole,
    onBackClicked: () -> Unit,
    viewModel: OrderDetailsViewModel = koinViewModel<OrderDetailsViewModel>()
) {
    BackHandler(enabled = true) {
        viewModel.onBackClicked()
    }

    val state by viewModel.state.collectAsStateWithLifecycle()
    val effects = viewModel.effect

    EffectsHandler(
        effects = effects,
        onBackClicked = onBackClicked,
        userRole = userRole
    )

    OrderDetailsContent(
        userRole = userRole,
        uiState = state,
        interactionListener = viewModel
    )
}

@Composable
private fun OrderDetailsContent(
    userRole: UserRole,
    uiState: OrderDetailsUiState,
    interactionListener: OrderDetailsInteractionListener
) {
    Scaffold(
        topBar = {
            OrderDetailsHeader(
                onBackClicked = interactionListener::onBackClicked,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        },
        bottomBar = {
            OrderDetailsFooter(
                userRole = userRole,
                isRated = (uiState.rating != 0),
                onClickRateTechnician = interactionListener::onRateTechnicianClicked,
                onClickGoHome = interactionListener::onGoHomeClicked
            )
        },
        overlays = {
            bottomSheet(isVisible = uiState.showRatingBottomSheet) { isVisible ->
                BottomSheet(
                    isVisible = isVisible,
                    onDismissRequest = interactionListener::onDismissRatingBottomSheetClicked,
                    skipPartiallyExpanded = true
                ) {
                    RatingBottomSheet(
                        isSuccess = uiState.isRatingSuccess,
                        isLoading = uiState.isSubmittingRating,
                        onSubmitRating = interactionListener::onSubmitRatingClicked,
                        onClickGoHome = interactionListener::onGoHomeClicked
                    )
                }
            }
        }
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            SuccessBanner(modifier = Modifier.padding(horizontal = 16.dp))
            OrderInfo(
                orderId = uiState.orderId,
                title = uiState.title,
                description = uiState.description,
                createdAt = uiState.timeLine.createdAt.format(),
                address = uiState.address.fullAddress,
            )
            if (uiState.rating != 0) {
                RatingInfo(
                    rating = uiState.rating,
                    comment = uiState.comment,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            PhotosSection(
                label = "Problem Photos",
                photos = uiState.problemPhotoUrls
            )
            OrderTimeline(timeline = uiState.timeLine)
            PhotosSection(
                label = "Before Repair",
                photos = uiState.repairPhotos.beforeRepairUrls
            )
            PhotosSection(
                label = "After Repair",
                photos = uiState.repairPhotos.afterRepairUrls
            )
        }
    }
}

@Composable
private fun EffectsHandler(
    effects: SharedFlow<OrderDetailsEffect>,
    onBackClicked: () -> Unit,
    userRole: UserRole
) {
    val navController = LocalNavController.current
    val snackBarHostController = LocalSnackBarHostController.current

    EffectHandler(
        effects = effects,
        key1 = navController.currentBackStackEntry
    ) { effect ->
        when (effect) {
            is OrderDetailsEffect.NavigateBack -> {
                onBackClicked()
                navController.popBackStack()
            }

            is OrderDetailsEffect.NavigateToHomeScreen -> {
                if (userRole == UserRole.CUSTOMER) {
                    navController.navigate(Route.CustomerHome)
                } else {
                    navController.navigate(Route.TechnicianHome)
                }
            }

            is OrderDetailsEffect.ShowSnackBar -> {
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

@Preview
@Composable
fun OrderDetailsScreenPreview() {
    OrderDetailsScreen(
        userRole = UserRole.CUSTOMER,
        onBackClicked = {}
    )
}