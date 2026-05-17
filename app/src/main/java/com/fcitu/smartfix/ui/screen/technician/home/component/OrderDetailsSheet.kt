package com.fcitu.smartfix.ui.screen.technician.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.fcitu.smartfix.domain.entity.Order
import com.fcitu.smartfix.ui.designSystem.components.bottomSheet.BottomSheet
import com.fcitu.smartfix.ui.designSystem.components.button.PrimaryButton
import com.fcitu.smartfix.ui.designSystem.components.scaffold.ScaffoldScope
import com.fcitu.smartfix.ui.designSystem.components.text.Text
import com.fcitu.smartfix.ui.screen.technician.home.TechHomeUiState

fun ScaffoldScope.orderDetailsSheet(
    state: TechHomeUiState,
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    order: Order?,
    onDismiss: () -> Unit,
    onAcceptClick: () -> Unit,
    onDelineClick: () -> Unit
) {

    bottomSheet(isVisible = isVisible) { currentVisibility ->

        var selectedImageUrl by remember { mutableStateOf<String?>(null) }

        selectedImageUrl?.let { url ->
            FullScreenImageViewer(
                imageUrl = url,
                onDismiss = { selectedImageUrl = null }
            )
        }

        BottomSheet(
            containerColor = Color.White,
            isVisible = currentVisibility,
            onDismissRequest = onDismiss,
            skipPartiallyExpanded = true,
            paddingFromTop = 64.dp,
            stickyFooterContent = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                ) {
                    androidx.compose.material3.HorizontalDivider(color = Color(0xFFF2F4F7), thickness = 1.dp)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 16.dp)
                            .navigationBarsPadding()
                    ) {
                        PrimaryButton(
                            text = "Decline",
                            containerColor = Color.Red,
                            disabledContainerColor = Color.Red,
                            contentColor = Color.White,
                            disabledContentColor = Color.White,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                onDelineClick()
                                onDismiss()
                            },
                        )
                        Spacer(modifier = Modifier.padding(horizontal = 6.dp))
                        PrimaryButton(
                            text = "Accept Order",
                            containerColor = Color(0xFFFF4A08),
                            disabledContainerColor = Color(0xFFFF4A08),
                            contentColor = Color.White,
                            disabledContentColor = Color.White,
                            modifier = Modifier.weight(2f),
                            onClick = {
                                onAcceptClick()
                                onDismiss()
                            },
                        )
                    }
                }
            },
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
                                            selectedImageUrl = imageUrl
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
                                Icons.Default.LocationOn,
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

@Composable
private fun FullScreenImageViewer(
    imageUrl: String,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .clickable(onClick = onDismiss),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Full screen image",
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )

            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}