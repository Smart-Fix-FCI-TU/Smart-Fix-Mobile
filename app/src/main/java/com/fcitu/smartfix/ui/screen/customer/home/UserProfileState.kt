package com.fcitu.smartfix.ui.screen.customer.home

import com.fcitu.smartfix.domain.entity.User

sealed interface UserProfileState {
    data object Loading : UserProfileState
    data class Success(val user: User) : UserProfileState
    data class Error(val message: String) : UserProfileState
}