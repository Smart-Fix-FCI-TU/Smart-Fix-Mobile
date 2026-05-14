package com.fcitu.smartfix.ui.screen.customer.techniciansList.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fcitu.smartfix.ui.designSystem.components.button.PrimaryButton
import com.fcitu.smartfix.ui.screen.technician.profile.TechnicianProfileUiState
import com.fcitu.smartfix.ui.screen.technician.profile.component.TechnicianProfileContent

@Composable
fun TechnicianProfileBottomSheet(
    profileContent: TechnicianProfileUiState,
    onClickOrderNow: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.BottomCenter
    ) {
        TechnicianProfileContent(
            state = profileContent,
            onViewAllReviewsClicked = { /* TODO: Handle view all reviews click */ },
        )
        PrimaryButton(
            text = "Order Now",
            onClick = onClickOrderNow,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 52.dp),
            containerColor = Color(0xFFFF4400)
        )
    }
}