package com.fcitu.smartfix.ui.screen.shared.orderDetails.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fcitu.smartfix.R
import com.fcitu.smartfix.ui.designSystem.components.button.PrimaryButton
import com.fcitu.smartfix.ui.designSystem.components.chip.Chip
import com.fcitu.smartfix.ui.designSystem.components.text.Text
import com.fcitu.smartfix.ui.designSystem.components.textField.MultiLineTextField
import com.fcitu.smartfix.ui.designSystem.theme.Cairo

@Composable
fun RatingBottomSheet(
    isSuccess: Boolean,
    isLoading: Boolean,
    onSubmitRating: (Int, String, List<String>) -> Unit,
    onClickGoHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFF0F0F2))
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .padding(16.dp)
    ) {
        AnimatedContent(isSuccess, label = "RatingContent") { isSuccessState ->
            if (isSuccessState) {
                RatingSuccessContent(onClickGoHome = onClickGoHome)
            } else {
                RatingFormContent(
                    isLoading = isLoading,
                    onSubmitRating = onSubmitRating
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun RatingFormContent(
    isLoading: Boolean,
    onSubmitRating: (Int, String, List<String>) -> Unit
) {
    var rating by remember { mutableIntStateOf(1) }
    var comment by remember { mutableStateOf("") }
    val selectedChips = remember { mutableStateListOf<String>() }

    val chips = getChips(rating)

    // Reset chips if rating changes
    LaunchedEffect(rating) {
        selectedChips.clear()
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Rate the Service",
            style = TextStyle(
                fontFamily = Cairo,
                color = Color(0xFF2D2F2F),
                fontSize = 24.sp,
                lineHeight = 32.sp,
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center
        )
        Text(
            text = "Your opinion helps us improve our service quality",
            style = TextStyle(
                fontFamily = Cairo,
                color = Color(0xFF667085),
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Normal
            ),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        RatingRow(
            rating = rating,
            onRatingChanged = { if (!isLoading) rating = it }
        )
        Spacer(modifier = Modifier.height(24.dp))

        if (rating > 0) {
            Text(
                text = "Why this rating?",
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(
                    fontFamily = Cairo,
                    color = Color(0xFF2D2F2F),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(12.dp))
            SuggestionChips(
                chips = chips,
                selectedChips = selectedChips
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        MultiLineTextField(
            value = comment,
            hint = "Add optional comment",
            onValueChanged = { if (!isLoading) comment = it },
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        )
        Spacer(modifier = Modifier.height(12.dp))
        PrimaryButton(
            text = "Submit Rating",
            onClick = { onSubmitRating(rating, comment, selectedChips.toList()) },
            modifier = Modifier.fillMaxWidth(),
            isEnabled = rating > 0 && !isLoading && selectedChips.isNotEmpty(),
            isLoading = isLoading,
            containerColor = Color(0xFFFF4400)
        )
    }
}

@Composable
private fun getChips(rating: Int): List<String> = when (rating) {
    in 1..2 -> listOf("Late", "Unprofessional", "Poor quality", "Dirty work")
    3 -> listOf("Fair price", "Average quality", "Acceptable", "Could be better")
    in 4..5 -> listOf(
        "On time",
        "Professional work",
        "Fast repair",
        "Excellent communication",
        "Clean and committed"
    )

    else -> emptyList()
}

@Composable
private fun RatingRow(
    rating: Int,
    onRatingChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
    maxStars: Int = 5
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(maxStars) { index ->
            val starIndex = index + 1
            IconButton(
                onClick = { onRatingChanged(starIndex) },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = if (rating >= starIndex) Icons.Filled.Star else Icons.Outlined.Star,
                    contentDescription = "Rate $starIndex stars",
                    tint = if (rating >= starIndex) Color(0xFFFF4400) else Color(0xFFD0D5DD),
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Composable
private fun SuggestionChips(
    chips: List<String>,
    selectedChips: SnapshotStateList<String>
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        chips.forEach { chipText ->
            val isSelected = selectedChips.contains(chipText)
            Chip(
                text = chipText,
                isSelected = isSelected,
                onClick = {
                    if (isSelected) selectedChips.remove(chipText)
                    else selectedChips.add(chipText)
                },
                containerColor = Color(0xFFFF4400),
                contentColor = Color.White,
                disabledContainerColor = Color(0xFFF2F4F7),
                disabledContentColor = Color(0xFF667085)
            )
        }
    }
}


@Composable
private fun RatingSuccessContent(
    onClickGoHome: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_success),
            contentDescription = null,
            tint = Color(0xFFFF4400),
            modifier = Modifier
                .size(100.dp)
                .background(Color(0xFFFFF0EB), RoundedCornerShape(20.dp))
                .padding(20.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Rating Submitted!",
            style = TextStyle(
                fontFamily = Cairo,
                color = Color(0xFF1D2939),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Your feedback helps improve the service for all users.",
            style = TextStyle(
                fontFamily = Cairo,
                color = Color(0xFF667085),
                fontSize = 16.sp,
                lineHeight = 24.sp
            ),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(48.dp))
        PrimaryButton(
            text = "Go to Home",
            onClick = onClickGoHome,
            modifier = Modifier.fillMaxWidth(),
            containerColor = Color(0xFFFF4400)
        )
    }
}