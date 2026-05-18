package com.fcitu.smartfix.ui.screen.technician.home.component

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.HorizontalDivider
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
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.fcitu.smartfix.domain.entity.Order
import com.fcitu.smartfix.ui.designSystem.components.bottomSheet.BottomSheet
import com.fcitu.smartfix.ui.designSystem.components.button.PrimaryButton
import com.fcitu.smartfix.ui.designSystem.components.scaffold.ScaffoldScope
import com.fcitu.smartfix.ui.designSystem.components.text.Text
import com.fcitu.smartfix.ui.screen.technician.home.TechHomeUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

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
        val context = LocalContext.current
        val scope = rememberCoroutineScope()
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
                    HorizontalDivider(color = Color(0xFFF2F4F7), thickness = 1.dp)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 16.dp)
                            .navigationBarsPadding()
                    ) {
                        PrimaryButton(
                            text = "Reject",
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
                            containerColor = Color(0xFF4CAF50),
                            disabledContainerColor = Color(0xFF4CAF50),
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

private suspend fun openImageInExternalApp(context: android.content.Context, imageUrl: String) {
    try {
        val imageFile = withContext(Dispatchers.IO) {
            val url = URL(imageUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"

            // We tell the server we're a regular browser so they don't reject us.
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
            connection.instanceFollowRedirects = true

            connection.connectTimeout = 15000
            connection.readTimeout = 15000
            connection.doInput = true
            connection.connect()

            if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                throw Exception("Server returned HTTP ${connection.responseCode}")
            }

            val fileName = "shared_image_${System.currentTimeMillis()}.jpg"
            val file = File(context.cacheDir, fileName)

            connection.inputStream.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            file
        }

        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", imageFile)

        withContext(Dispatchers.Main) {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "image/*")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(Intent.createChooser(intent, "Open image with"))
        }

    } catch (e: Exception) {
        e.printStackTrace()
//  If the download fails, open it in the browser        withContext(Dispatchers.Main) {
        Toast.makeText(context, "Cannot open in Gallery, opening in browser...", Toast.LENGTH_SHORT)
            .show()

        try {
            val browserIntent = Intent(Intent.ACTION_VIEW, imageUrl.toUri())
            browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(browserIntent)
        } catch (ex: Exception) {
            Toast.makeText(context, "Failed to open image", Toast.LENGTH_SHORT).show()
        }
    }
}
