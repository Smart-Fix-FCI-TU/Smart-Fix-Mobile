package com.fcitu.smartfix.ui.designSystem.components.appBar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fcitu.smartfix.R

@Composable
fun AppBarOptionContainer(
    modifier: Modifier = Modifier,
    isBadgeVisible: Boolean = false,
    badgeColor: Color = Color(0xFF000000),
    containerColor: Color = Color(0xFFFFFFFF),
    shape: Shape = RoundedCornerShape(12.dp),
    badgeShape: Shape = RoundedCornerShape(100.dp),
    iconContentPadding: PaddingValues = PaddingValues(10.dp),
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val clickableModifier = onClick?.let {
        Modifier.clickable(
            onClick = it,
            interactionSource = remember { MutableInteractionSource() },
            indication = ripple(),
        )
    } ?: Modifier

    Box(
        modifier = modifier.size(40.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(containerColor, shape)
                .clip(shape)
                .then(clickableModifier)
                .padding(iconContentPadding)
        ) {
            content()
        }
        AnimatedVisibility(
            visible = isBadgeVisible,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = (-1).dp)
        ) {
            Box(
                Modifier
                    .size(5.dp)
                    .background(badgeColor, badgeShape)
            )
        }
    }
}

@Preview
@Composable
private fun IconContainerPreview() {
    Box(
        modifier = Modifier
            .size(120.dp)
            .background(Color(0xFFFF2F4F7)),
        contentAlignment = Alignment.Center
    ) {
        AppBarOptionContainer(isBadgeVisible = true, onClick = {}, content = {
            Icon(
                painter = painterResource(R.drawable.ic_test),
                contentDescription = null,
            )
        })
    }
}