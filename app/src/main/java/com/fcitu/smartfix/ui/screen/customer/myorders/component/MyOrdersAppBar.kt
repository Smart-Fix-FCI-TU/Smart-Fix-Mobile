package com.fcitu.smartfix.ui.screen.customer.myorders.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fcitu.smartfix.ui.designSystem.components.appBar.AppBarOptionContainer
import com.fcitu.smartfix.ui.designSystem.components.text.Text
import com.fcitu.smartfix.ui.designSystem.theme.Cairo

@Composable
fun MyOrdersAppBar(
    title: String,
    modifier: Modifier = Modifier,
    titleColor: Color = Color(0xFF0E1017),
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
    leadingContent: (@Composable () -> Unit)? = null,
    onLeadingClick: (() -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    onTrailingClick: (() -> Unit)? = null,
    isNotificationBadgeVisible: Boolean = false
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth().background(color = Color.White)
            .padding(contentPadding)
    ) {
        leadingContent?.let { content ->
            AppBarOptionContainer(
                onClick = onLeadingClick,
                modifier = Modifier.padding(end = 8.dp),
                content = content,
            )
        }
        Text(
            text = title,
            color = titleColor,
            style = TextStyle(
                fontFamily = Cairo,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                lineHeight = 28.sp
            )
        )
        Spacer(modifier = Modifier.weight(1f))
        trailingContent?.let {
            AppBarOptionContainer(
                modifier = Modifier.padding(end = 8.dp),
                badgeColor = Color.Red,
                containerColor = Color(0xFFF2F4F7),
                isBadgeVisible = isNotificationBadgeVisible,
                content = { it() },
                onClick = onTrailingClick
            )
        }
    }
}