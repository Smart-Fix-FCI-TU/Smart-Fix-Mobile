package com.fcitu.smartfix.ui.screen.customer.techniciansList

sealed interface TechnicianListEffect {
    data object NavigateBack : TechnicianListEffect
}