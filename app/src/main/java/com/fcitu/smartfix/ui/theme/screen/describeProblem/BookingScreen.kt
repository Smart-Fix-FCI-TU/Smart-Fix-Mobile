package com.fcitu.smartfix.ui.theme.screen.describeProblem

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.location.Geocoder
import android.net.Uri
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fcitu.smartfix.ui.theme.SmartFixTheme
import com.fcitu.smartfix.ui.theme.designSystem.components.button.PrimaryButton
import com.fcitu.smartfix.ui.theme.screen.describeProblem.components.AdditionalNotesSection
import com.fcitu.smartfix.ui.theme.screen.describeProblem.components.DescribeProblemTopBar
import com.fcitu.smartfix.ui.theme.screen.describeProblem.components.FloorAndApartmentSection
import com.fcitu.smartfix.ui.theme.screen.describeProblem.components.LocationSelectionSection
import com.fcitu.smartfix.ui.theme.screen.describeProblem.components.OrderDetailsBottomSheetContent
import com.fcitu.smartfix.ui.theme.screen.describeProblem.components.ProblemDescriptionSection
import com.fcitu.smartfix.ui.theme.screen.describeProblem.components.ProblemHeaderSection
import com.fcitu.smartfix.ui.theme.screen.describeProblem.components.ProblemPhotoPickerSection
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import java.util.Locale

@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    serviceId: String,
    viewModel: BookingViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToMap: () -> Unit,
    onProblemSubmitted: () -> Unit,
    selectedLocation: String? = null,
    latitude: Double? = null,
    longitude: Double? = null
) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val geocoder = remember { Geocoder(context, Locale.getDefault()) }

    // Launcher for Permissions
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

    LaunchedEffect(serviceId) {
        viewModel.setServiceId(serviceId)
    }

    LaunchedEffect(selectedLocation, latitude, longitude) {
        selectedLocation?.let { 
            viewModel.onLocationSelected(it, latitude, longitude) 
        }
    }

    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val pickMultipleVisualMedia = rememberLauncherForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia(
            maxItems = (5 - uiState.problemPhotos.size).coerceAtLeast(1)
        )
    ) { uris ->
        if (uris.isNotEmpty()) {
            viewModel.onProblemPhotoAdded(uris)
        }
    }

    // Effect Handler
    LaunchedEffect(Unit) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                BookingUiEffect.NavigateToMap -> {
                    // Open Google Maps App for visual confirmation
                    val gmmIntentUri = Uri.parse("geo:0,0?q=my+location")
                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    mapIntent.setPackage("com.google.android.apps.maps")
                    try {
                        context.startActivity(mapIntent)
                    } catch (e: Exception) {
                        // Fallback if maps app is not installed
                        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=my+location"))
                        context.startActivity(webIntent)
                    }

                    // Fetch real location data
                    locationPermissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }

                is BookingUiEffect.ShowSnackBar -> {
                    snackbarHostState.showSnackbar(effect.message)
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

    Scaffold(
        topBar = {
            DescribeProblemTopBar(onNavigateBack = onNavigateBack)
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            ProblemHeaderSection()

            Spacer(modifier = Modifier.height(16.dp))

            ProblemDescriptionSection(
                shortTitle = uiState.shortTitle,
                detailedDescription = uiState.detailedDescription,
                onShortTitleChanged = viewModel::onShortTitleChanged,
                onDetailedDescriptionChanged = viewModel::onDetailedDescriptionChanged
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProblemPhotoPickerSection(
                problemPhotos = uiState.problemPhotos,
                onAddPhotoClicked = viewModel::onAddPhotoClicked,
                onRemovePhoto = viewModel::onProblemPhotoRemoved
            )

            Spacer(modifier = Modifier.height(16.dp))

            LocationSelectionSection(
                location = uiState.location,
                onLocationChanged = viewModel::onLocationSelected,
                onMapPlaceholderClicked = viewModel::onMapPlaceholderClicked
            )

            Spacer(modifier = Modifier.height(16.dp))

            FloorAndApartmentSection(
                floor = uiState.floor,
                apartmentNo = uiState.apartmentNo,
                onFloorChanged = viewModel::onFloorChanged,
                onApartmentNoChanged = viewModel::onApartmentNoChanged
            )

            Spacer(modifier = Modifier.height(16.dp))

            AdditionalNotesSection(
                additionalNotes = uiState.additionalNotes,
                onAdditionalNotesChanged = viewModel::onAdditionalNotesChanged
            )

            Spacer(modifier = Modifier.height(24.dp))

            PrimaryButton(
                text = "Find Service",
                onClick = viewModel::onFindServiceClicked,
                isEnabled = uiState.isFindServiceButtonEnabled,
                containerColor = Color(0xFFFF5500),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Order Details Bottom Sheet
        if (uiState.showOrderDetailsBottomSheet) {
            ModalBottomSheet(onDismissRequest = viewModel::onBottomSheetDismissed) {
                OrderDetailsBottomSheetContent(
                    shortTitle = uiState.shortTitle,
                    detailedDescription = uiState.detailedDescription,
                    problemPhotos = uiState.problemPhotos,
                    location = uiState.location,
                    isLoading = uiState.isLoading,
                    onFindAvailableTechnicianClicked = viewModel::onFindAvailableTechnicianClicked
                )
            }
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
            } catch (e: Exception) {
                onResult("Location Found (${it.latitude}, ${it.longitude})", it.latitude, it.longitude)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookingScreenPreview() {
    SmartFixTheme {
        // Preview code
    }
}