package com.fcitu.smartfix.ui.screen.shared.orderDetails.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.fcitu.smartfix.ui.designSystem.components.text.Text
import com.fcitu.smartfix.ui.designSystem.theme.Cairo

@Composable
fun PhotosSection(
    label: String,
    photos: List<String>,
    modifier: Modifier = Modifier
) {
    Column {
        Text(
            text = label,
            style = TextStyle(
                fontFamily = Cairo,
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        LazyRow(
            modifier = modifier,
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(photos.size) { index ->
                PhotoItem(
                    photoUrl = photos[index]
                )
            }
        }
    }
}

@Composable
private fun PhotoItem(
    photoUrl: String
) {
    Box(
        modifier = Modifier
            .size(138.dp)
            .clip(RoundedCornerShape(8.dp))
    ) {
        AsyncImage(
            model = photoUrl,
            contentDescription = "Photo",
            contentScale = ContentScale.Crop
        )
    }
}