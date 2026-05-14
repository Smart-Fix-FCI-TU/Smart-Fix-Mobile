package com.fcitu.smartfix.ui.screen.technician.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.fcitu.smartfix.R
import com.fcitu.smartfix.ui.designSystem.components.appBar.AppBar
import com.fcitu.smartfix.ui.designSystem.components.scaffold.Scaffold
import com.fcitu.smartfix.ui.screen.technician.profile.component.ReviewItem

@Composable
fun AllReviewsScreen(
    reviews: List<ReviewUiState>,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            AppBar(
                title = "All Reviews",
                leadingContent = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "Back"
                    )
                },
                onLeadingClick = onBack
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            reviews.forEach { review ->
                ReviewItem(review = review)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}
