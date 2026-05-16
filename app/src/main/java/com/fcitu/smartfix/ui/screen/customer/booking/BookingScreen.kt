package com.fcitu.smartfix.ui.screen.customer.booking

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fcitu.smartfix.ui.designSystem.components.bottomSheet.BottomSheet
import com.fcitu.smartfix.ui.designSystem.components.button.OutlinedButton
import com.fcitu.smartfix.ui.designSystem.components.button.PrimaryButton
import com.fcitu.smartfix.ui.designSystem.components.button.TextButton
import com.fcitu.smartfix.ui.designSystem.components.dialog.BasicDialog
import com.fcitu.smartfix.ui.designSystem.components.scaffold.Scaffold
import com.fcitu.smartfix.ui.designSystem.components.snackBar.AnimatedSnackBarHost
import com.fcitu.smartfix.ui.designSystem.components.snackBar.LocalSnackBarHostController
import com.fcitu.smartfix.ui.designSystem.components.snackBar.SnackBarData
import com.fcitu.smartfix.ui.designSystem.components.snackBar.SnackBarHostController
import com.fcitu.smartfix.ui.designSystem.components.text.Text
import com.fcitu.smartfix.ui.designSystem.theme.Cairo
import com.fcitu.smartfix.ui.screen.customer.booking.components.AdditionalNotesSection
import com.fcitu.smartfix.ui.screen.customer.booking.components.BookingTopBar
import com.fcitu.smartfix.ui.screen.customer.booking.components.FloorAndApartmentSection
import com.fcitu.smartfix.ui.screen.customer.booking.components.LocationSelectionSection
import com.fcitu.smartfix.ui.screen.customer.booking.components.OrderDetailsBottomSheetContent
import com.fcitu.smartfix.ui.screen.customer.booking.components.ProblemDescriptionSection
import com.fcitu.smartfix.ui.screen.customer.booking.components.ProblemHeaderSection
import com.fcitu.smartfix.ui.screen.customer.booking.components.ProblemPhotoPickerSection
import com.fcitu.smartfix.ui.utils.EffectHandler
import com.fcitu.smartfix.ui.utils.LocationUtils
import kotlinx.coroutines.flow.SharedFlow
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun BookingScreen(
    serviceId: String,
    onNavigateBack: () -> Unit,
    onNavigateToMap: () -> Unit,
    onProblemSubmitted: () -> Unit,
    viewModel: BookingViewModel = koinViewModel<BookingViewModel>(),
    selectedLocation: String? = null,
    latitude: Double? = null,
    longitude: Double? = null
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effects = viewModel.effect
    val snackBarHostController = remember { SnackBarHostController() }

    LaunchedEffect(serviceId) {
        viewModel.setServiceId(serviceId)
    }

    LaunchedEffect(selectedLocation, latitude, longitude) {
        selectedLocation?.let {
            viewModel.onLocationSelected(it, latitude, longitude)
        }
    }

    CompositionLocalProvider(LocalSnackBarHostController provides snackBarHostController) {
        EffectsHandler(
            effects = effects,
            onProblemSubmitted = onProblemSubmitted,
            onNavigateToMap = onNavigateToMap,
            viewModel = viewModel
        )

        BookingScreenContent(
            uiState = state,
            interactionListener = viewModel,
            onNavigateBack = onNavigateBack,
            snackBarHostController = snackBarHostController,
            onUseCurrentLocation = { viewModel.emitEffect(BookingUiEffect.RequestLocation) }
        )
    }
}

@Composable
private fun BookingScreenContent(
    uiState: BookingUiState,
    interactionListener: BookingInteractionListener,
    onNavigateBack: () -> Unit,
    snackBarHostController: SnackBarHostController,
    onUseCurrentLocation: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            BookingTopBar(onNavigateBack = onNavigateBack)
        },
        snakeBar = {
            AnimatedSnackBarHost(snackBarHostController)
        },
        overlays = {
            dialog(isVisible = uiState.showDeletePhotoDialog) { isVisible ->
                BasicDialog(
                    isVisible = isVisible,
                    onDismiss = interactionListener::onDismissDeletePhoto,
                    onCancelClick = interactionListener::onDismissDeletePhoto,
                    actionButtons = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            PrimaryButton(
                                text = "Confirm",
                                onClick = interactionListener::onConfirmDeletePhoto,
                                containerColor = Color(0xFFFF5500),
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            TextButton(
                                text = "Cancel",
                                onClick = interactionListener::onDismissDeletePhoto,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Delete Photo",
                            style = TextStyle(
                                fontFamily = Cairo,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Are you sure you want to delete this photo?",
                            style = TextStyle(
                                fontFamily = Cairo,
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        )
                    }
                }
            }

            dialog(isVisible = uiState.showLocationDialog) { isVisible ->
                BasicDialog(
                    isVisible = isVisible,
                    onDismiss = interactionListener::onDismissLocationDialog,
                    onCancelClick = interactionListener::onDismissLocationDialog,
                    actionButtons = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            PrimaryButton(
                                text = "Use My Current Location",
                                onClick = {
                                    interactionListener.onDismissLocationDialog()
                                    onUseCurrentLocation()
                                },
                                containerColor = Color(0xFFFF5500),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                ) {
                    Text(
                        text = "Choose Your Location",
                        style = TextStyle(
                            fontFamily = Cairo,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        ),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            bottomSheet(isVisible = uiState.showOrderDetailsBottomSheet) { isVisible ->
                BottomSheet(
                    isVisible = isVisible,
                    onDismissRequest = interactionListener::onBottomSheetDismissed,
                    sheetContent = {
                        OrderDetailsBottomSheetContent(
                            shortTitle = uiState.shortTitle,
                            detailedDescription = uiState.detailedDescription,
                            problemPhotos = uiState.problemPhotos,
                            location = uiState.location,
                            isLoading = uiState.isLoading,
                            onFindAvailableTechnicianClicked = interactionListener::onFindAvailableTechnicianClicked
                        )
                    }, skipPartiallyExpanded = true
                )
            }
        }
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            ProblemHeaderSection()

            Spacer(modifier = Modifier.height(16.dp))

            ProblemDescriptionSection(
                shortTitle = uiState.shortTitle,
                detailedDescription = uiState.detailedDescription,
                onShortTitleChanged = interactionListener::onShortTitleChanged,
                onDetailedDescriptionChanged = interactionListener::onDetailedDescriptionChanged
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProblemPhotoPickerSection(
                problemPhotos = uiState.problemPhotos,
                onAddPhotoClicked = interactionListener::onAddPhotoClicked,
                onRemovePhoto = interactionListener::onProblemPhotoRemoved
            )

            Spacer(modifier = Modifier.height(16.dp))

            LocationSelectionSection(
                location = uiState.location,
                onLocationChanged = { interactionListener.onLocationSelected(it) },
                onMapPlaceholderClicked = interactionListener::onGetCurrentLocationClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            FloorAndApartmentSection(
                floor = uiState.floor,
                apartmentNo = uiState.apartmentNo,
                onFloorChanged = interactionListener::onFloorChanged,
                onApartmentNoChanged = interactionListener::onApartmentNoChanged
            )

            Spacer(modifier = Modifier.height(16.dp))

            AdditionalNotesSection(
                additionalNotes = uiState.additionalNotes,
                onAdditionalNotesChanged = interactionListener::onAdditionalNotesChanged
            )

            Spacer(modifier = Modifier.height(24.dp))

            PrimaryButton(
                text = "Confirm",
                onClick = interactionListener::onFindServiceClicked,
                isEnabled = uiState.isFindServiceButtonEnabled,
                containerColor = Color(0xFFFF5500),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
private fun EffectsHandler(
    effects: SharedFlow<BookingUiEffect>,
    onProblemSubmitted: () -> Unit,
    onNavigateToMap: () -> Unit,
    viewModel: BookingViewModel
) {
    val context = LocalContext.current
    val snackBarHostController = LocalSnackBarHostController.current

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        ) {
            // Permission granted, trigger location fetch
            LocationUtils.getCurrentLocation(
                context = context,
                onResult = { address, lat, lng ->
                    viewModel.onLocationSelected(address, lat, lng)
                },
                onError = { error ->
                    viewModel.emitEffect(
                        BookingUiEffect.ShowSnackBar(
                            "Failed to get location: ${error.message}",
                            isError = true
                        )
                    )
                }
            )
        } else {
            viewModel.emitEffect(
                BookingUiEffect.ShowSnackBar(
                    "Location permission is required to get your current location.",
                    isError = true
                )
            )
        }
    }

    fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    val pickMultipleVisualMedia = rememberLauncherForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia(maxItems = 10)
    ) { uris ->
        if (uris.isNotEmpty()) {
            viewModel.onProblemPhotoAdded(uris)
        }
    }

    EffectHandler(effects = effects) { effect ->
        when (effect) {
            BookingUiEffect.RequestLocation -> {
                if (hasLocationPermission()) {
                    LocationUtils.getCurrentLocation(
                        context = context,
                        onResult = { address, lat, lng ->
                            viewModel.onLocationSelected(address, lat, lng)
                        },
                        onError = { error ->
                            viewModel.emitEffect(
                                BookingUiEffect.ShowSnackBar(
                                    "Failed to get location: ${error.message}",
                                    isError = true
                                )
                            )
                        }
                    )
                } else {
                    locationPermissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
            }

            is BookingUiEffect.ShowSnackBar -> {
                snackBarHostController.showSnackBar(
                    SnackBarData(
                        message = effect.message,
                        isError = effect.isError
                    )
                )
            }

            is BookingUiEffect.LaunchImagePicker -> {
                pickMultipleVisualMedia.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }

            is BookingUiEffect.ShowImageRemoveConfirmation -> {
                // Now handled via state and dialog in overlays
            }

            BookingUiEffect.NavigateToMap -> onNavigateToMap()

            BookingUiEffect.ProblemSubmittedSuccessfully -> onProblemSubmitted()
        }
    }
}


