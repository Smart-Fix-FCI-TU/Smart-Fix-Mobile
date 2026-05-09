package com.fcitu.smartfix.ui.screen.customer.booking

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.location.Geocoder
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fcitu.smartfix.ui.designSystem.components.bottomSheet.BottomSheet
import com.fcitu.smartfix.ui.designSystem.components.button.PrimaryButton
import com.fcitu.smartfix.ui.designSystem.components.scaffold.Scaffold
import com.fcitu.smartfix.ui.designSystem.components.snackBar.AnimatedSnackBarHost
import com.fcitu.smartfix.ui.designSystem.components.snackBar.LocalSnackBarHostController
import com.fcitu.smartfix.ui.designSystem.components.snackBar.SnackBarData
import com.fcitu.smartfix.ui.designSystem.components.snackBar.SnackBarHostController
import com.fcitu.smartfix.ui.screen.customer.booking.components.AdditionalNotesSection
import com.fcitu.smartfix.ui.screen.customer.booking.components.BookingTopBar
import com.fcitu.smartfix.ui.screen.customer.booking.components.FloorAndApartmentSection
import com.fcitu.smartfix.ui.screen.customer.booking.components.LocationSelectionSection
import com.fcitu.smartfix.ui.screen.customer.booking.components.OrderDetailsBottomSheetContent
import com.fcitu.smartfix.ui.screen.customer.booking.components.ProblemDescriptionSection
import com.fcitu.smartfix.ui.screen.customer.booking.components.ProblemHeaderSection
import com.fcitu.smartfix.ui.screen.customer.booking.components.ProblemPhotoPickerSection
import com.fcitu.smartfix.ui.screen.customer.booking.BookingUiEffect
import com.fcitu.smartfix.ui.screen.customer.booking.BookingUiState
import com.fcitu.smartfix.ui.screen.customer.booking.BookingViewModel
import com.fcitu.smartfix.ui.utils.EffectHandler
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.flow.SharedFlow
import org.koin.compose.viewmodel.koinViewModel
import java.util.Locale

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

    LaunchedEffect(serviceId) {
        viewModel.setServiceId(serviceId)
    }

    LaunchedEffect(selectedLocation, latitude, longitude) {
        selectedLocation?.let {
            viewModel.onLocationSelected(it, latitude, longitude)
        }
    }

    EffectsHandler(
        effects = effects,
        onNavigateToMap = onNavigateToMap,
        onProblemSubmitted = onProblemSubmitted,
        viewModel = viewModel
    )

    BookingScreenContent(
        uiState = state,
        interactionListener = viewModel,
        onNavigateBack = onNavigateBack
    )
}

@Composable
private fun BookingScreenContent(
    uiState: BookingUiState,
    interactionListener: BookingInteractionListener,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val snackBarHostController = remember { SnackBarHostController() }

    Scaffold(
        topBar = {
            BookingTopBar(onNavigateBack = onNavigateBack)
        },
        snakeBar = {
            AnimatedSnackBarHost(snackBarHostController)
        },
        overlays = {
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
                    }
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
                onMapPlaceholderClicked = interactionListener::onMapPlaceholderClicked
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
                text = "Find Service",
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
    onNavigateToMap: () -> Unit,
    onProblemSubmitted: () -> Unit,
    viewModel: BookingViewModel
) {
    val context = LocalContext.current
    val snackBarHostController = LocalSnackBarHostController.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val geocoder = remember { Geocoder(context, Locale.getDefault()) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        ) {
            // Permission granted, trigger location fetch
            getCurrentLocation(fusedLocationClient, geocoder) { address, lat, lng ->
                viewModel.onLocationSelected(address, lat, lng)
            }
        }
    }

    val uiState by viewModel.state.collectAsStateWithLifecycle()

    val pickMultipleVisualMedia = rememberLauncherForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia(
            maxItems = (5 - uiState.problemPhotos.size).coerceAtLeast(1)
        )
    ) { uris ->
        if (uris.isNotEmpty()) {
            viewModel.onProblemPhotoAdded(uris)
        }
    }

    EffectHandler(effects = effects) { effect ->
        when (effect) {
            BookingUiEffect.NavigateToMap -> {
                onNavigateToMap()
                val gmmIntentUri = "geo:0,0?q=my+location".toUri()
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
                    setPackage("com.google.android.apps.maps")
                }
                try {
                    context.startActivity(mapIntent)
                } catch (_: Exception) {
                    val webIntent = Intent(
                        Intent.ACTION_VIEW,
                        "https://www.google.com/maps/search/?api=1&query=my+location".toUri()
                    )
                    context.startActivity(webIntent)
                }

                locationPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
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
                viewModel.onProblemPhotoRemoved(effect.uri)
            }

            BookingUiEffect.ProblemSubmittedSuccessfully -> onProblemSubmitted()
        }
    }
}

@SuppressLint("MissingPermission")
private fun getCurrentLocation(
    fusedLocationClient: FusedLocationProviderClient,
    geocoder: Geocoder,
    onResult: (String, Double, Double) -> Unit
) {
    val cancellationTokenSource = CancellationTokenSource()
    fusedLocationClient.getCurrentLocation(
        Priority.PRIORITY_HIGH_ACCURACY,
        cancellationTokenSource.token
    ).addOnSuccessListener { location ->
        location?.let {
            try {
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                val addressLine = addresses?.firstOrNull()?.getAddressLine(0) ?: "Location Found"
                onResult(addressLine, it.latitude, it.longitude)
            } catch (_: Exception) {
                onResult(
                    "Location Found (${it.latitude}, ${it.longitude})",
                    it.latitude,
                    it.longitude
                )
            }
        }
    }
}
