package com.fcitu.smartfix.ui.screen.technician.profile.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fcitu.smartfix.ui.screen.technician.profile.TechnicianProfileInteractionListener
import com.fcitu.smartfix.ui.screen.technician.profile.TechnicianProfileUiState

@Composable
fun TechnicianProfileContent(
    state: TechnicianProfileUiState,
    listener: TechnicianProfileInteractionListener,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // جزء الصورة والبيانات الشخصية
        TechnicianHeaderSection(
            name = state.name,
            occupation = state.occupation,
            location = state.location,
            profilePhotoUrl = state.profilePhotoUrl,
            isOnline = state.isOnline
        )

        Spacer(modifier = Modifier.height(24.dp))

        // جزء الأرقام والتقييمات
        TechnicianStatsSection(
            rating = state.rating,
            reviewCount = state.reviewCount,
            experience = state.experience
        )

        Spacer(modifier = Modifier.height(24.dp))

        // جزء "نبذة عن الفني"
        TechnicianAboutSection(
            bio = state.bio
        )

        Spacer(modifier = Modifier.height(24.dp))

        // جزء التقييمات
        TechnicianReviewsSection(
            reviews = state.reviews,
            onViewAllClick = listener::onViewAllReviewsClicked
        )
    }
}
