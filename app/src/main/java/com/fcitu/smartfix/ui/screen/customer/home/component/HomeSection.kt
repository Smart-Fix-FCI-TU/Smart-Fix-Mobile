package com.fcitu.smartfix.ui.screen.customer.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fcitu.smartfix.R
import com.fcitu.smartfix.ui.designSystem.components.button.Button
import com.fcitu.smartfix.ui.designSystem.components.text.Text
import com.fcitu.smartfix.ui.designSystem.theme.Cairo

@Composable
fun HomeSection(
    modifier: Modifier = Modifier,
    title: String,
    actionName: String? = null,
    onActionNameClick: () -> Unit={},

    ) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) {
        Text(
            text = title,
            style = TextStyle(
                fontFamily = Cairo,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                lineHeight = 28.sp
            ),
            modifier = Modifier.weight(0.7f)
        )
        if (actionName != null) {
            Button(onClick = { onActionNameClick() }) {
                Text(
                    text = actionName,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = Color(0xFFFF6D00)
                    )
                )

                Image(
                    painter = painterResource(R.drawable.ic_orange_arrow_left),
                    contentDescription = "",
                    modifier = Modifier.padding(start = 5.dp)
                )
            }
        }
    }
}
