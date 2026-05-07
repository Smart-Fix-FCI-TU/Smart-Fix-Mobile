package com.fcitu.smartfix.ui.screen.customer.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.fcitu.smartfix.R
import com.fcitu.smartfix.domain.entity.User
import com.fcitu.smartfix.ui.designSystem.components.text.Text

@Composable
fun UserProfileHeader(user: User) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(0.5f), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = "Hello ${user.firstName}",
                style = TextStyle(
                    color = Color(0xFF1C1B1F),
                    fontWeight = FontWeight.Black,
                    fontSize = 24.sp,
                    lineHeight = 32.sp
                )

            )
            Text(
                text = "Delivery to: ${user.address.fullAddress}",
                style = TextStyle(
                    color = Color.Black,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            )
        }
        //------Profile Photo--------------------------
        ProfilePhoto(user.profilePhotoUrl)

    }
}

//-----------------Profile Photo Composable---------------------
@Composable
private fun ProfilePhoto(imageUrl: String?, modifier: Modifier = Modifier) {
    val imageModifier = modifier
        .size(80.dp)
        .clip(CircleShape)
        .border(width = 5.dp, color = Color.White, shape = CircleShape)
    if (imageUrl.isNullOrBlank()) {
        Image(
            painter = painterResource(R.drawable.icon_profile),
            contentDescription = "Profile Icon",
            modifier = imageModifier,
            contentScale = ContentScale.Fit
        )
    }else{
        AsyncImage(
            model =imageUrl  ,
            contentDescription = "Profile Photo",
            contentScale = ContentScale.Fit,
            modifier = imageModifier,
        )
    }
}