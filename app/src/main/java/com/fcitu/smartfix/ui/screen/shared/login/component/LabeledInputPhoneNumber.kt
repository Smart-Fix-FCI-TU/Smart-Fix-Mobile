package com.fcitu.smartfix.ui.screen.shared.login.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fcitu.smartfix.R
import com.fcitu.smartfix.ui.designSystem.components.text.Text
import com.fcitu.smartfix.ui.designSystem.components.textField.TextField
import com.fcitu.smartfix.ui.designSystem.theme.Cairo

@Composable
fun LabeledInputPhoneNumber(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontFamily = Cairo,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                lineHeight = 24.sp
            )
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CountryFlag(
                countryCode = "+20",
                countryPainter = painterResource(R.drawable.ic_egypt_flag),
            )
            // TODO: The hint shows a spaced phone number ("012 3456 7890"), but MobileNumberValidator expects a digits-only string
            TextField(
                value = value,
                onValueChanged = onValueChange,
                hint = "012 3456 7890",
                leadingIcon = painterResource(R.drawable.ic_phone),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun CountryFlag(
    countryCode: String,
    countryPainter: Painter,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFFFFFFF))
            .padding(
                vertical = 13.dp,
                horizontal = 8.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = countryPainter,
            contentDescription = "country image",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .clip(RoundedCornerShape(100.dp))
                .size(20.dp)
        )

        Text(
            text = countryCode,
            style = TextStyle(
                fontFamily = Cairo,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 22.sp
            ).copy(
                textDirection = TextDirection.Ltr
            ),
            modifier = Modifier.padding(start = 4.dp, end = 2.dp),
            color = Color(0xFF0E1017),
        )
    }
}