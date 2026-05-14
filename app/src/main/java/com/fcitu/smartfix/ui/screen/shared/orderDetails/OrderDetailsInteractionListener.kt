package com.fcitu.smartfix.ui.screen.shared.orderDetails

interface OrderDetailsInteractionListener {
    fun onBackClicked()
    fun onRateTechnicianClicked()
    fun onSubmitRatingClicked(rating: Float, comment: String)
    fun onDismissRatingBottomSheetClicked()
    fun onGoHomeClicked()
}