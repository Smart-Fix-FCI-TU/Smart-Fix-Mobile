package com.fcitu.smartfix.ui.screen.customer.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fcitu.smartfix.R
import com.fcitu.smartfix.ui.designSystem.components.appBar.AppBarOptionContainer
import com.fcitu.smartfix.ui.designSystem.components.text.Text
import com.fcitu.smartfix.ui.designSystem.theme.Cairo

@Composable
fun HomeAppBar(
    title: String,
    modifier: Modifier = Modifier,
    titleColor: Color = Color(0xFFFF4400),
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
    leadingContent: (@Composable () -> Unit)? = null,
    onLeadingClick: (() -> Unit)? = null,
    badgeVisible: Boolean = false,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth().background(color = Color.White)
            .padding(contentPadding)
    ) {

        Text(
            text = title,
            color = titleColor,
            style = TextStyle(
                fontFamily = Cairo,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp,
                lineHeight = 28.sp
            )
        )
        leadingContent?.let { content ->
            AppBarOptionContainer(
                onClick = onLeadingClick,
                modifier = Modifier.padding(end = 8.dp),
                content = content,
                containerColor = Color(0xFFF2F4F7),
                badgeColor = Color.Red,
                isBadgeVisible = badgeVisible
            )
        }
    }
}

@Preview
@Composable
private fun AppBarPreview() {

    HomeAppBar(
        title = "Smart Fix",
       leadingContent = {Image(
            painter = painterResource(R.drawable.notification_icon),
            contentDescription = "",
            modifier = Modifier.size(20.dp)
        )}

    )

}
