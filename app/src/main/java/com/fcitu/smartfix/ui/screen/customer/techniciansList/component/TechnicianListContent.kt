package com.fcitu.smartfix.ui.screen.customer.techniciansList.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fcitu.smartfix.ui.designSystem.components.bottomSheet.BottomSheet
import com.fcitu.smartfix.ui.designSystem.components.scaffold.Scaffold
import com.fcitu.smartfix.ui.screen.customer.techniciansList.TechnicianListInteractionListener
import com.fcitu.smartfix.ui.screen.customer.techniciansList.TechnicianListUiState
import com.fcitu.smartfix.ui.screen.technician.profile.ReviewUiState
import com.fcitu.smartfix.ui.screen.technician.profile.TechnicianProfileUiState
import com.fcitu.smartfix.ui.utils.formatAsTime

@Composable
fun TechnicianListContent(
    state: TechnicianListUiState,
    listener: TechnicianListInteractionListener
) {
    Scaffold(
        topBar = {
            TechniciansListHeader(
                onBackClicked = listener::onClickBack
            )
        },
        overlays = {
            bottomSheet(isVisible = state.isTechnicianProfileSheetVisible && state.selectedTechnician != null) { isVisible ->
                BottomSheet(
                    isVisible = isVisible,
                    onDismissRequest = listener::onDismissProfileSheet,
                    skipPartiallyExpanded = true
                ) {
                    state.selectedTechnician?.let { technician ->
                        TechnicianProfileBottomSheet(
                            profileContent = TechnicianProfileUiState(
                                name = "${technician.user.firstName} ${technician.user.lastName}",
                                occupation = "${technician.serviceCategory}",
                                location = technician.user.address.fullAddress,
                                rating = "${technician.averageRating}",
                                reviewCount = "${technician.reviewCount}",
                                experience = "${technician.yearsOfExperience} years",
                                bio = technician.bio,
                                reviews = technician.reviews.map { review ->
                                    ReviewUiState(
                                        id = review.id,
                                        reviewerName = review.reviewerName,
                                        rating = review.rating,
                                        comment = review.comment,
                                        date = review.createdAt.formatAsTime()
                                    )
                                },
                                profilePhotoUrl = technician.user.profilePhotoUrl,
                                isOnline = technician.isAvailable,
                            ),
                            onClickOrderNow = { listener.onClickOrderNow(technician) },
                        )
                    }
                }
            }

            bottomSheet(isVisible = state.isWaitingTechnicianSheetVisible && state.selectedTechnician != null) { isVisible ->
                BottomSheet(
                    isVisible = isVisible,
                    onDismissRequest = listener::onDismissWaitingSheet,
                    skipPartiallyExpanded = true,
                ) {
                    state.selectedTechnician?.let { technician ->
                        WaitingTechnicianBottomSheet(
                            technician = technician,
                            countdownTime = state.countdownTime,
                            modifier = Modifier.padding(16.dp),
                            onClickCancelOrder = listener::onClickCancelOrder
                        )
                    }
                }
            }

            bottomSheet(isVisible = state.isAcceptedSheetVisible && state.selectedTechnician != null) { isVisible ->
                BottomSheet(
                    isVisible = isVisible,
                    onDismissRequest = listener::onDismissAcceptedSheet,
                    skipPartiallyExpanded = true,
                ) {
                    state.selectedTechnician?.let { technician ->
                        AcceptanceBottomSheetContent(
                            technician = technician,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }

            bottomSheet(isVisible = state.isRejectedSheetVisible && state.selectedTechnician != null) { isVisible ->
                BottomSheet(
                    isVisible = isVisible,
                    onDismissRequest = listener::onDismissRejectedSheet,
                    skipPartiallyExpanded = true,
                ) {
                    state.selectedTechnician?.let { technician ->
                        RejectedBottomSheetContent(
                            modifier = Modifier.padding(16.dp),
                            technician = technician,
                            onClickChooseAnother = listener::onDismissRejectedSheet,
                            onClickGoHome = listener::onClickBack
                        )
                    }
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            FilterChips(
                selectedFilter = state.selectedFilter,
                onClickFilter = listener::onClickFilter,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(state.technicians) { technician ->
                    TechnicianCard(
                        technician = technician,
                        onTechnicianClick = { listener.onClickTechnician(technician) },
                        onClickOrderNow = { listener.onClickOrderNow(technician) }
                    )
                }
            }
        }
    }
}