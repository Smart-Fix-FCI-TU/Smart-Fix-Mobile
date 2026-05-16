package com.fcitu.smartfix.ui.screen.shared.orderDetails

interface OrderDetailsInteractionListener {
    fun onBackClicked()
    fun onRateTechnicianClicked()
    fun onSubmitRatingClicked(rating: Int, comment: String, chips: List<String>)
    fun onDismissRatingBottomSheetClicked()
    fun onGoHomeClicked()
}