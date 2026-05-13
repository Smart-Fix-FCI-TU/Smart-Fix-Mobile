package com.fcitu.smartfix.ui.screen.customer.profile.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fcitu.smartfix.ui.screen.customer.profile.CustomerProfileInteractionListener
import com.fcitu.smartfix.ui.screen.customer.profile.CustomerProfileUiState

@Composable
fun CustomerProfileContent(
    state: CustomerProfileUiState,
    listener: CustomerProfileInteractionListener,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        CustomerHeaderSection(
            name = state.name,
            memberSince = state.memberSince,
            profilePhotoUrl = state.profilePhotoUrl,
            isOnline = state.isOnline
        )

        Spacer(modifier = Modifier.height(32.dp))

        AccountInformationSection(
            phoneNumber = state.phoneNumber,
            email = state.email,
            address = state.address
        )

        Spacer(modifier = Modifier.height(32.dp))

        ServiceHistorySection(
            history = state.serviceHistory,
            onViewAllClick = listener::onViewAllServiceHistoryClicked
        )
    }
}
