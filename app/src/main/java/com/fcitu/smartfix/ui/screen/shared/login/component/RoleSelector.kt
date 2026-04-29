package com.fcitu.smartfix.ui.screen.shared.login.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fcitu.smartfix.domain.model.UserRole
import com.fcitu.smartfix.ui.designSystem.components.chip.Chip
import com.fcitu.smartfix.ui.designSystem.components.text.Text
import com.fcitu.smartfix.ui.designSystem.theme.Cairo

@Composable
fun RoleSelector(
    selectedRole: UserRole,
    onRoleSelected: (UserRole) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "Role",
            style = TextStyle(
                fontFamily = Cairo,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                lineHeight = 24.sp
            )
        )
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            RoleOption(
                text = "Customer",
                isSelected = (selectedRole == UserRole.CUSTOMER),
                onClick = { onRoleSelected(UserRole.CUSTOMER) },
                modifier = Modifier.weight(1f)
            )
            RoleOption(
                text = "Technician",
                isSelected = (selectedRole == UserRole.TECHNICIAN),
                onClick = { onRoleSelected(UserRole.TECHNICIAN) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun RoleOption(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Chip(
        modifier = modifier,
        text = text,
        onClick = onClick,
        isSelected = isSelected,
        containerColor = Color(0xFFFF4400),
        disabledContainerColor = Color(0xFFD9D9D9),
        contentColor = Color(0xFFFFFFFF),
        disabledContentColor = Color(0xFF000000),
        shape = RoundedCornerShape(12.dp)
    )
}