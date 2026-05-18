package com.fcitu.smartfix.ui.screen.shared.orderDetails.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fcitu.smartfix.domain.model.UserRole
import com.fcitu.smartfix.ui.designSystem.components.button.PrimaryButton

@Composable
fun OrderDetailsFooter(
    userRole: UserRole,
    isRated: Boolean,
    onClickRateTechnician: () -> Unit,
    onClickGoHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (userRole == UserRole.CUSTOMER && !isRated) {
            PrimaryButton(
                text = "Rate Technician",
                onClick = onClickRateTechnician,
                modifier = Modifier.fillMaxWidth(),
                containerColor = Color(0xFFFF4400)
            )
        }

        PrimaryButton(
            text = "Go Home",
            onClick = onClickGoHome,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    border = BorderStroke(
                        width = 1.dp,
                        color = Color(0xFFFF4400)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ),
            containerColor = Color.White,
            contentColor = Color(0xFFFF4400)
        )
    }
}