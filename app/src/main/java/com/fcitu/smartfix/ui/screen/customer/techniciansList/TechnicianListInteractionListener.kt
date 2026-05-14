package com.fcitu.smartfix.ui.screen.customer.techniciansList

import com.fcitu.smartfix.domain.entity.Technician


interface TechnicianListInteractionListener {
    fun onClickFilter(filter: FilterType)
    fun onClickTechnician(technician: Technician)
    fun onClickOrderNow(technician: Technician)
    fun onDismissProfileSheet()
    fun onDismissWaitingSheet()
    fun onDismissAcceptedSheet()
    fun onDismissRejectedSheet()
    fun onClickCancelOrder()
    fun onClickBack()
}