package com.fcitu.smartfix.ui.screen.customer.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.fcitu.smartfix.R
import com.fcitu.smartfix.domain.entity.Address
import com.fcitu.smartfix.domain.entity.User
import com.fcitu.smartfix.domain.model.UserRole
import com.fcitu.smartfix.ui.designSystem.components.text.Text
import com.fcitu.smartfix.ui.screen.customer.home.UserProfileState

@Composable
fun UserProfileHeader(userState: UserProfileState, modifier: Modifier = Modifier) {
    when (userState) {
        is UserProfileState.Error -> {
            Row(
                modifier = modifier
                    .padding(vertical = 12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Failed Loading User Info",
                    style = TextStyle(
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        lineHeight = 24.sp
                    )
                )
            }
        }

        is UserProfileState.Loading -> {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(50.dp)
                        .padding(top = 30.dp),
                    color = Color.White,
                    strokeWidth = 5.dp,
                    trackColor = Color(0xFFFF4501)
                )
            }
        }

        is UserProfileState.Success -> {
            Row(
                modifier = modifier
                    .padding(vertical = 12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(0.5f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Hello ${userState.user.firstName}",
                        style = TextStyle(
                            color = Color(0xFF1C1B1F),
                            fontWeight = FontWeight.Black,
                            fontSize = 30.sp,
                            lineHeight = 32.sp
                        )

                    )
                    Text(
                        text = "Delivery to: ${userState.user.address.fullAddress}",
                        style = TextStyle(
                            color = Color.Black,
                            fontWeight = FontWeight.Medium,
                            fontSize = 17.sp,
                            lineHeight = 20.sp
                        )
                    )
                }
                //------Profile Photo--------------------------
                ProfilePhoto(userState.user.profilePhotoUrl)

            }
        }
    }

}

//-----------------Profile Photo Composable---------------------
@Composable
private fun ProfilePhoto(imageUrl: String?, modifier: Modifier = Modifier) {
    val imageModifier = modifier
        .size(80.dp)
        .clip(CircleShape)
        .border(width = 5.dp, color = Color.White, shape = CircleShape)
    if (imageUrl.isNullOrBlank() || imageUrl.isEmpty()) {
        Image(
            painter = painterResource(R.drawable.icon_profile),
            contentDescription = "Profile Icon",
            modifier = imageModifier,
            contentScale = ContentScale.Fit
        )
    } else {
        AsyncImage(
            model = imageUrl,
            contentDescription = "Profile Photo",
            contentScale = ContentScale.Fit,
            modifier = imageModifier,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Test() {
   UserProfileHeader(userState = UserProfileState.Success(user = User(
       id = "5425425",
       phoneNumber = "563767567262",
       firstName = "Fouad",
       lastName = "Elmeligy",
       username = "Fouad Elmeligy",
       birthOfDate = "2/2/2002",
       nationalId = "25362627246",
       email = "fouad@gmail.com",
       role = UserRole.CUSTOMER,
       profilePhotoUrl = "",
       address = Address(
           id = "523455",
           fullAddress = "Tanta",
           location = Address.Location(30.0, 31.0),
           floor = "1",
           apartmentNo = "2"
       )
   )))
}

