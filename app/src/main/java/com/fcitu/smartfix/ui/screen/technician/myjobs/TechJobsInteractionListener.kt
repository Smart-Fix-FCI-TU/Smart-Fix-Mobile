package com.fcitu.smartfix.ui.screen.technician.myjobs

interface TechJobsInteractionListener {
    fun onTabSelected(tab: TechJobsTab)
    fun onActiveJobClicked(orderId: String)
    fun onHistoryJobClicked(orderId: String)
    fun onChatClicked(orderId: String, customerId: String)
    fun onDialerClicked(phoneNumber: String)
    fun onTryAgainClick()
}