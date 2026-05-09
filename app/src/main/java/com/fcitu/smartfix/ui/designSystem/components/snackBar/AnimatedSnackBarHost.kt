package com.fcitu.smartfix.ui.designSystem.components.snackBar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fcitu.smartfix.R
import com.fcitu.smartfix.ui.designSystem.components.text.Text
import com.fcitu.smartfix.ui.designSystem.theme.Cairo
import kotlinx.coroutines.delay

@Composable
fun AnimatedSnackBarHost(
    snackBarHostController: SnackBarHostController,
    modifier: Modifier = Modifier,
) {
    val state = snackBarHostController.state.collectAsStateWithLifecycle().value

    val iconId = if (state.snackBarData.isError) R.drawable.ic_snackbar_error
    else R.drawable.ic_snackbar_success

    AnimatedVisibility(
        visible = state.isVisible,
        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
    ) {
        LaunchedEffect(state.snackBarData) {
            delay(state.snackBarData.duration)
            snackBarHostController.dismissSnackBar()
        }
        SnackBar(
            modifier = modifier
                .dropShadow(
                    shape = RoundedCornerShape(8.dp),
                    shadow = Shadow(
                        radius = 8.dp,
                        spread = 0.dp,
                        alpha = 0.6f,
                        color = Color.Black.copy(alpha = 0.2f),
                        offset = DpOffset(x = 0.dp, 4.dp)
                    )
                )
                .clickable(
                    onClick = { snackBarHostController.dismissSnackBar() },
                    indication = null,
                    interactionSource = null
                ),
            title = state.snackBarData.title,
            message = state.snackBarData.message,
            leadingIcon = painterResource(iconId),
        )
    }
}

@Composable
private fun SnackBar(
    title: String,
    message: String,
    leadingIcon: Painter,
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified,
    contentDescription: String? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFFFFFFF))
            .padding(
                top = 8.dp,
                bottom = 8.dp,
                start = 12.dp,
                end = 24.dp
            )
    ) {
        Icon(
            painter = leadingIcon,
            contentDescription = contentDescription,
            modifier = Modifier.size(28.dp),
            tint = tint
        )

        Column {
            Text(
                text = title,
                style = TextStyle(
                    fontFamily = Cairo,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                ),
                color = Color(0xFF0E1017),
                fontSize = 16.sp,
                lineHeight = 24.sp
            )

            Text(
                text = message,
                style = TextStyle(
                    fontFamily = Cairo,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    lineHeight = 22.sp
                ),
                color = Color(0xFF3E4252)
            )
        }
    }
}