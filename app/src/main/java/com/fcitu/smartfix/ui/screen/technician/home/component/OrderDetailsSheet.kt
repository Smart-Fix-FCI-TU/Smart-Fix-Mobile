package com.fcitu.smartfix.ui.screen.technician.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.fcitu.smartfix.domain.entity.Order
import com.fcitu.smartfix.ui.designSystem.components.bottomSheet.BottomSheet
import com.fcitu.smartfix.ui.designSystem.components.scaffold.ScaffoldScope
import com.fcitu.smartfix.ui.designSystem.components.text.Text
import com.fcitu.smartfix.ui.screen.technician.home.TechHomeUiState
import com.fcitu.smartfix.ui.utils.openImageInExternalApp
import kotlinx.coroutines.launch

fun ScaffoldScope.orderDetailsSheet(
    state: TechHomeUiState,
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    order: Order?,
    onDismiss: () -> Unit,
) {


    bottomSheet(isVisible = isVisible) { currentVisibility ->
        val context = LocalContext.current
        val scope = rememberCoroutineScope()
        BottomSheet(
            containerColor = Color.White,
            isVisible = currentVisibility,
            onDismissRequest = onDismiss,
            skipPartiallyExpanded = true,
            paddingFromTop = 64.dp,
            sheetContent = {
                if (order == null) return@BottomSheet

                if (order.id in state.rejectedOrderIds) {
                    onDismiss()
                }

                Column(
                    modifier
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = order.details.title,
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            lineHeight = 28.sp,
                            color = Color.Black
                        )
                    )

                    if (order.details.problemPhotoUrls.isNotEmpty()) {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(order.details.problemPhotoUrls) { imageUrl ->
                                AsyncImage(
                                    model = imageUrl,
                                    contentDescription = "Problem photo",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(120.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable {
                                            scope.launch {
                                                openImageInExternalApp(context, imageUrl)
                                            }
                                        }
                                )
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = Color(0xFFF4F4F5),
                                shape = RoundedCornerShape(16.dp)
                            )
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Location Icon",
                                tint = Color(0xFFFF4A08),
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(color = Color.White, shape = CircleShape)
                                    .padding(8.dp)
                            )
                            Column(
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    text = order.details.address.fullAddress,
                                    style = TextStyle(
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                )
                                Text(
                                    text = "Floor: ${order.details.address.floor}, Apt: ${order.details.address.apartmentNo}",
                                    style = TextStyle(
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = Color(0xFF5A5C5C)
                                    )
                                )
                            }
                        }
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Problem Description:-",
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        )
                        Text(
                            text = order.details.description,
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                lineHeight = 22.sp,
                                color = Color(0xFF2D2F2F)
                            ),
                        )
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Notes:-",
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        )
                        Text(
                            text = order.details.additionalNotes,
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                lineHeight = 22.sp,
                                color = Color(0xFF2D2F2F)
                            ),
                        )
                    }
                }
            }
        )
    }
}

